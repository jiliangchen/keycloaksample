package com.rakufit.keycloak;

import javax.sql.DataSource;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.rakufit.keycloak.listener.JobCompletionNotificationListener;

@Configuration
@EnableBatchProcessing
@ComponentScan
public class BatchConfiguration extends DefaultBatchConfigurer {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	Keycloak keycloak() {
		Keycloak kc = KeycloakBuilder.builder().serverUrl("http://localhost:8080/auth").realm("demo")
				.username("jiliangchen").password("cjl123456").clientId("admin-cli")
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
		return kc;
	}

	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<KeyCloakUser> reader() {
		return new FlatFileItemReaderBuilder<KeyCloakUser>().name("personItemReader")
				.resource(new ClassPathResource("sample-data.csv")).delimited()
				.names(new String[] { "firstName", "lastName" })
				.fieldSetMapper(new BeanWrapperFieldSetMapper<KeyCloakUser>() {
					{
						setTargetType(KeyCloakUser.class);
					}
				}).build();
	}

	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		// override to do not set datasource even if a datasource exist.
		// initialize will use a Map based JobRepository (instead of database)
	}

	@Bean
	public ItemWriter<KeyCloakUser> writer(Keycloak kc) {
		return new KeyCloakUserItemWriter(kc);
	}

	public JdbcBatchItemWriter<KeyCloakUser> jdbcWriter(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<KeyCloakUser>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)").dataSource(dataSource)
				.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Autowired
	@Bean
	public Job postUserJob(JobCompletionNotificationListener listener, Step step1) {
		System.out.println("================" + Application.KEYCLOAK_USER + "/" + Application.KEYCLOAK_PASSWORD
				+ "=================");
		return jobBuilderFactory.get("postUserJob").incrementer(new RunIdIncrementer()).listener(listener).flow(step1)
				.end().build();
	}

	@Bean
	public Step step1(ItemWriter<KeyCloakUser> writer) {
		return stepBuilderFactory.get("step1").<KeyCloakUser, KeyCloakUser>chunk(10).reader(reader())
				.processor(processor()).writer(writer).exceptionHandler(exceptionHandler()).build();
	}
	// end::jobstep[]

	private ExceptionHandler exceptionHandler() {
		return new ExceptionHandler() {
			@Override
			public void handleException(RepeatContext context, Throwable throwable) throws Throwable {
				// 例外を投げず、終了する
				System.out.println("~~~~~~~~~~~~~jiliangchen~~~~~~~~~~~~~~~");
				throwable.printStackTrace();
				context.setTerminateOnly();
			}

		};
	}

	@Bean
	public PlatformTransactionManager getTransactionManager() {
		return new ResourcelessTransactionManager();
	}

	@Bean
	public JobRepository getJobRepo() throws Exception {
		return new MapJobRepositoryFactoryBean(getTransactionManager()).getObject();
	}

	@Bean
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
		SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
		simpleJobLauncher.setJobRepository(jobRepository);
		return simpleJobLauncher;
	}
}
