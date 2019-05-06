package com.rakufit.keycloak;

import javax.sql.DataSource;

import org.keycloak.admin.client.Keycloak;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<KeyCloakUser> reader() {
        return new FlatFileItemReaderBuilder<KeyCloakUser>()
            .name("personItemReader")
            .resource(new ClassPathResource("sample-data.csv"))
            .delimited()
            .names(new String[]{"firstName", "lastName"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<KeyCloakUser>() {{
                setTargetType(KeyCloakUser.class);
            }})
            .build();
    }

    @Bean
    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    @Bean
    public ItemWriter<KeyCloakUser> writer(Keycloak kc) {
        return new KeyCloakUserItemWriter(kc);
    }


    public JdbcBatchItemWriter<KeyCloakUser> jdbcWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<KeyCloakUser>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
            .dataSource(dataSource)
            .build();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job postUserJob(JobCompletionNotificationListener listener, Step step1) {
        System.out.println("================" + Application.KEYCLOAK_USER + "/" + Application.KEYCLOAK_PASSWORD + "=================");
        return jobBuilderFactory.get("postUserJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1(ItemWriter<KeyCloakUser> writer) {
        return stepBuilderFactory.get("step1")
            .<KeyCloakUser, KeyCloakUser> chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(writer).exceptionHandler(exceptionHandler())
            .build();
    }
    // end::jobstep[]

    private ExceptionHandler exceptionHandler() {
        return new ExceptionHandler() {
            @Override
            public void handleException(RepeatContext context, Throwable throwable) throws Throwable {
                // 例外を投げず、終了する
                System.out.println("~~~~~~~~~~~~~jiliangchen~~~~~~~~~~~~~~~");
                context.setTerminateOnly();
            }

        };
    }
}
