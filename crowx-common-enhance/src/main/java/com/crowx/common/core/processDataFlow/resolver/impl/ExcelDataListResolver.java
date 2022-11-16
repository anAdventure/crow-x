package com.crowx.common.core.processDataFlow.resolver.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.crowx.common.core.processDataFlow.core.ExecDataContext;
import com.crowx.common.core.processDataFlow.domain.FieldDefinition;
import com.crowx.common.core.processDataFlow.domain.FileUploadDataDTO;
import com.crowx.common.core.processDataFlow.domain.ImportDataFailDTO;
import com.crowx.common.core.processDataFlow.exception.ExcelImportException;
import com.crowx.common.core.processDataFlow.resolver.DataListResolver;
import com.crowx.common.core.text.Convert;
import com.crowx.common.utils.DateUtils;
import com.crowx.common.utils.LocalDateUtils;
import com.crowx.common.utils.StringUtils;
import com.crowx.common.utils.poi.ExcelUtil;
import com.google.common.collect.Lists;
import lombok.val;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExcelDataListResolver<T> implements DataListResolver<T>{

    private FileUploadDataDTO fileUploadDataDTO;

    public ExcelDataListResolver(FileUploadDataDTO fileUploadDataDTO){
        this.fileUploadDataDTO=fileUploadDataDTO;
    }

    private Integer headerIndex = 0 ;

    private ExecDataContext<T> context;
    @Override
    public List<T> loadData(ExecDataContext<T> execDataContext) throws  ExcelImportException {

        this.context=execDataContext;

        List<T> list = new ArrayList();

        FileInputStream is=null;

        String sheetName = "";

        try {
            is = new FileInputStream(fileUploadDataDTO.getFilePath());

            List<FieldDefinition<T, ?>> fieldList = execDataContext.getFieldList();

            Workbook wb = WorkbookFactory.create(is);

            Sheet sheet = null;
            if (StringUtils.isNotEmpty(sheetName)) {
                sheet = wb.getSheet(sheetName);
            } else {
                sheet = wb.getSheetAt(0);
            }

            if (sheet == null) {
                throw  new ExcelImportException(new ImportDataFailDTO("文件sheet不存在", null));

            }

            int rows = sheet.getPhysicalNumberOfRows();
            if(rows<2){
                throw  new ExcelImportException(new ImportDataFailDTO("导入数据为空!", null));
            }
            Map<String, Integer> importHeader = getImportHeader(sheet);

            // 表头校验
            checkHeader(fieldList.stream().map(FieldDefinition::getAlias).collect(Collectors.toList()),importHeader);
            for (int i = headerIndex+1 ; i <rows; i++) {
                Row row = sheet.getRow(i);

                T bean = newBean();

                for (FieldDefinition<T, ?> fieldDefinition : fieldList) {
                    setterBeanValue(fieldDefinition,bean,ExcelUtil.getCellValue(row,importHeader.get(fieldDefinition.getAlias())));
                }
                list.add(bean);
            }
        } catch (IOException e) {
            throw new ExcelImportException();
        } finally {
            try {
                if(is!=null)is.close();
            } catch (IOException e) {
                throw new ExcelImportException("io流关闭异常");
            }
        }

        return list;
    }

    private void checkHeader(List<String> definitionHeader, Map<String, Integer> importHeader) throws ExcelImportException {
        List<String> failHeader = definitionHeader.stream().filter(s -> !importHeader.keySet().contains(s)).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(failHeader)){
            throw new ExcelImportException(new ImportDataFailDTO("表头不存在",failHeader.toString()));
        }
    }

    private List<ImportDataFailDTO>  setterBeanValue(FieldDefinition<T, ?> field, T bean, Object val) {

        // 获取对应实体类字段 类型
        Class<?> fieldType = field.getFieldType();
        // setter 方法
        BiConsumer<T, ?> setter = field.getSetter();

        // todo 后面适配
//        Function<Object, ?> convert = field.getConvert();
        Function<Object, ?> convert = null;

        try {
            if (null != convert) {
                Object result = convert.apply(val);
                invokeSetter(setter, bean, result);
            } else if (String.class == fieldType) {
                String s = Convert.toStr(val);
                if (StringUtils.endsWith(s, ".0")) {
                    s = StringUtils.substringBefore(s, ".0");
                } else {
                    s = Convert.toStr(val);
                }
                // todo 特殊字符校验
               /* if (pattern != null && pattern.matcher(val.toString()).find()) {
                    ImportDataFailDTO importDataFailDTO = new ImportDataFailDTO(++rowIndex, ++colIndex, "不允许输入特殊字符", val.toString());
                    log.error(importDataFailDTO.toString());
                    return Collections.singletonList(importDataFailDTO);
                }*/
                if (StringUtils.isNotEmpty(s)) {
                    s = s.trim();
                }
                invokeSetter(setter, bean, s);
            } else if (Integer.TYPE == fieldType || Integer.class == fieldType) {
                invokeSetter(setter, bean, Convert.toInt(val));
            } else if (Long.TYPE == fieldType || Long.class == fieldType) {
                invokeSetter(setter, bean, Convert.toLong(val));
            } else if (Double.TYPE == fieldType || Double.class == fieldType) {
                invokeSetter(setter, bean, Convert.toDouble(val));
            } else if (Float.TYPE == fieldType || Float.class == fieldType) {
                invokeSetter(setter, bean, Convert.toFloat(val));
            } else if (BigDecimal.class == fieldType) {
                invokeSetter(setter, bean, Convert.toBigDecimal(val));
            } else if (Date.class == fieldType) {
                Date date = null;
                if (val instanceof String) {
                    date = DateUtils.parseDate(val);
                } else if (val instanceof Double) {
                    date = DateUtil.getJavaDate((Double) val);
                } else if (val instanceof Date) {
                    date = (Date) val;
                }
                invokeSetter(setter, bean, date);
            } else if (LocalDate.class == fieldType) {
                LocalDate localDate = null;
                if (val instanceof String && StringUtils.isNotEmpty(val.toString())) {
                    localDate = LocalDateUtils.asLocalDate(DateUtils.parseDate(val));
                } else if (val instanceof Double) {
                    localDate = LocalDateUtils.asLocalDate(DateUtil.getJavaDate((Double) val));
                } else if (val instanceof Date) {
                    localDate = LocalDateUtils.asLocalDate((Date) val);
                }
                invokeSetter(setter, bean, localDate);
            } else if (LocalDateTime.class == fieldType) {
                LocalDateTime localDateTime = null;
                if (val instanceof String) {
                    localDateTime = LocalDateUtils.asLocalDateTime(DateUtils.parseDate(val));
                } else if (val instanceof Double) {
                    localDateTime = LocalDateUtils.asLocalDateTime(DateUtil.getJavaDate((Double) val));
                } else if (val instanceof Date) {
                    localDateTime = LocalDateUtils.asLocalDateTime((Date) val);
                }
                invokeSetter(setter, bean, localDateTime);
            } else {
                return Collections.singletonList(new ImportDataFailDTO("不支持的字段类型，请自定义类型转换器", fieldType.getName()));
            }
        } catch (ExcelImportException e) {
//            throw e;
            return e.getFailList();
        }

        return Collections.emptyList();
    }
    private <FT> void  invokeSetter(BiConsumer<T, ?> setter, T bean, FT value) throws ExcelImportException {
            BiConsumer<T, FT> typeSetter = (BiConsumer<T, FT>) setter;

            typeSetter.accept(bean,value);


    }

    private T newBean(){
        return context.getBeanCreate().get();
    }
    private Map<String,Integer> getImportHeader(Sheet sheet) {

        HashMap<String, Integer> header = new LinkedHashMap<>();
        Row heard = sheet.getRow(headerIndex);

        for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++) {
            Cell cell = heard.getCell(i);
            if(ObjectUtils.isNotEmpty(cell)){
                String value =(String) ExcelUtil.getCellValue(heard,i);
                header.put(value,i);
            }
        }
        return header;
    }


}
