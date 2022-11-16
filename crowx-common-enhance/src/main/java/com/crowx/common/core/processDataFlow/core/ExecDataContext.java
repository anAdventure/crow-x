package com.crowx.common.core.processDataFlow.core;

import com.crowx.common.core.processDataFlow.build.FieldDefinitionBuilder;
import com.crowx.common.core.processDataFlow.core.api.ExecFlow;
import com.crowx.common.core.processDataFlow.core.api.ExecFlowStep;
import com.crowx.common.core.processDataFlow.domain.FieldDefinition;
import com.crowx.common.core.processDataFlow.domain.FileUploadDataDTO;
import com.crowx.common.core.processDataFlow.handler.DataListHandler;
import com.crowx.common.core.processDataFlow.resolver.DataListResolver;
import com.crowx.common.core.processDataFlow.resolver.impl.ExcelDataListResolver;
import com.crowx.common.core.processDataFlow.resolver.impl.SimpleDataListResolver;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Data
public class ExecDataContext<T> {


    /**
     * 实体类
     */
    private Class<T> beanClass;

    /**
     * bean 对象创建器
     */
    private Supplier<T> beanCreate;

    /**
     * bean 描述字段
     */
    private List<FieldDefinition<T, ?>> fieldList;
   /**
     * bean 描述字段
     */
    private List<ExecFlowStep> execFlowStepList = new ArrayList<>();

    /**
     * 数据解析器
     */
    private DataListResolver<T> dataListResolver;

    /**
     * 返回 Context 构造器对象
     *
     * @param beanClass 实体类 Class
     * @param beanCreate 实体类对象创建器（如果不传，则通过反射创建对象，实体类必须有 无参构造方法）
     * @param <B> 实体类型
     * @return {@link Builder}
     */
    public static <B> Builder<B> builder(Class<B> beanClass, Supplier<B> beanCreate) {
        return new Builder<>(beanClass, beanCreate);
    }

    @SneakyThrows
    public static <B> Builder<B> builder(Class<B> beanClass) {
       Constructor<B> constructor = beanClass.getConstructor();
      return new Builder<>(beanClass, () -> {
          try {
              return constructor.newInstance();
          } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
              e.printStackTrace();
          }
          return null;
      });
    }

    /**
     * 上下文对象构建器
     *
     * @param <T> bean
     */
    @Slf4j
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static class Builder<T> {

        /**
         * 上下文对象
         */
        private final ExecDataContext<T> context;

        public Builder(Class<T> beanClass) {
            this.context = new ExecDataContext<>();
            this.context.beanClass = beanClass;
        }

        public Builder(Class<T> beanClass, Supplier<T> beanCreate) {
            this(beanClass);
            this.context.beanCreate = beanCreate;
        }


        /**
         * 解析字段配置
         *
         * @param builder 导入字段集合构造器
         * @return {@link Builder}
         */
        public Builder<T> fieldDefinitionBuilder(FieldDefinitionBuilder<T> builder) {
            // todo 根据类去缓存
            this.context.fieldList = builder.build();
            return this;
        }


        /**
         * 添加数据处理器
         * @param dataListHandler {@link DataListHandler}
         * @@return {@link Builder}
         */
        public Builder<T> addListHandler(DataListHandler<T> dataListHandler) {
            this.context.execFlowStepList.add(dataListHandler);
            return this;
        }

        public ExecFlow<T> defaultExecFlow(List<T> dataList) {

            context.dataListResolver = new SimpleDataListResolver<>(dataList);

            return new DefaultExecFlow<>(context);
        }

        public ExecFlow<T> defaultExecFlow(FileUploadDataDTO fileUploadDataDTO) {

            context.dataListResolver = new ExcelDataListResolver<>(fileUploadDataDTO);

            return new DefaultExecFlow<>(context);
        }

        public ExecFlow<T> defaultExecFlow(ExcelDataListResolver excelDataListResolver) {
            return new DefaultExecFlow<>(context);
        }

    }
}
