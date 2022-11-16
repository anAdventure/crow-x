package com.crowx.framework.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.sql.DataSource;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

/**
 * Mybatis支持*匹配扫描包
 * 
 * @author ruoyi
 */


@Configuration
public class MyBatisConfig {
    @Autowired
    private Environment env;
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    public MyBatisConfig() {
    }

    public static String setTypeAliasesPackage(String typeAliasesPackage) {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        ArrayList allResult = new ArrayList();

        try {
            String[] var4 = typeAliasesPackage.split(",");
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String aliasesPackage = var4[var6];
                List<String> result = new ArrayList();
                aliasesPackage = "classpath*:" + ClassUtils.convertClassNameToResourcePath(aliasesPackage.trim()) + "/" + "**/*.class";
                Resource[] resources = resolver.getResources(aliasesPackage);
                if (resources != null && resources.length > 0) {
                    MetadataReader metadataReader = null;
                    Resource[] var11 = resources;
                    int var12 = resources.length;

                    for(int var13 = 0; var13 < var12; ++var13) {
                        Resource resource = var11[var13];
                        if (resource.isReadable()) {
                            metadataReader = metadataReaderFactory.getMetadataReader(resource);

                            try {
                                result.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getPackage().getName());
                            } catch (ClassNotFoundException var16) {
                                var16.printStackTrace();
                            }
                        }
                    }
                }

                if (result.size() > 0) {
                    HashSet<String> hashResult = new HashSet(result);
                    allResult.addAll(hashResult);
                }
            }

            if (allResult.size() <= 0) {
                throw new RuntimeException("mybatis-plus typeAliasesPackage 路径扫描错误,参数typeAliasesPackage:" + typeAliasesPackage + "未找到任何包");
            }

            typeAliasesPackage = String.join(",", (String[])((String[])allResult.toArray(new String[0])));
        } catch (IOException var17) {
            var17.printStackTrace();
        }

        return typeAliasesPackage;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource ) throws Exception {
        String typeAliasesPackage = this.env.getProperty("mybatis-plus.typeAliasesPackage");
        String mapperLocations = this.env.getProperty("mybatis-plus.mapperLocations");
        String configLocation = this.env.getProperty("mybatis-plus.configLocation");
        typeAliasesPackage = setTypeAliasesPackage(typeAliasesPackage);
        VFS.addImplClass(SpringBootVFS.class);
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        sessionFactory.setMapperLocations((new PathMatchingResourcePatternResolver()).getResources(mapperLocations));
        sessionFactory.setConfigLocation((new DefaultResourceLoader()).getResource(configLocation));
        return sessionFactory.getObject();
    }
}
