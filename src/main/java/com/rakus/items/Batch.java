//package com.rakus.items;
//
//import javax.sql.DataSource;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecutionListener;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import com.rakus.items.domain.Original;
//
//@Configuration
//@EnableBatchProcessing
//public class Batch {
//
//	@Autowired
//	public JobBuilderFactory jobBuilderFactory;
//
//	@Autowired
//	public StepBuilderFactory stepBuilderFactory;
//
//	@Autowired
//	public DataSource dataSource;
//
//	// Reader
//	@Bean
//	public FlatFileItemReader<Original> reader() {
//
//		FlatFileItemReader<Original> reader = new FlatFileItemReader<Original>();
//		reader.setResource(new ClassPathResource("train.tsv"));
//		reader.setLineMapper(new DefaultLineMapper<Original>() {
//			{
//				setLineTokenizer(new DelimitedLineTokenizer() {
//					{
//						setNames(new String[] { "id", "name","condition_id","category_name","brand","price","shopping","description" });
//					}
//				});
//				setFieldSetMapper(new BeanWrapperFieldSetMapper<Original>() {
//					{
//						setTargetType(Original.class);
//					}
//				});
//			}
//		});
//
//		return reader;
//	}
//
//	// Writer
//	@Bean
//	public JdbcBatchItemWriter<Original> writer() {
//		JdbcBatchItemWriter<Original> writer = new JdbcBatchItemWriter<Original>();
//		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Original>());
//		writer.setSql(
//				"INSERT INTO original (id,name,condition_id,category_name,brand,price,shopping,description) VALUES (:id,:name,:condition_id,:category_name,:brand,:price,:shopping,:description);"
//		);
//		writer.setDataSource(dataSource);
//		return writer;
//	}
//
//	@Bean
//	public JobExecutionListener listener() {
//		return new JobStartEndListener(new JdbcTemplate(dataSource));
//	}
//
//	// ステップ１
//	@Bean
//	public Step step1() {
//		return stepBuilderFactory.get("step1").<Original, Original>chunk(10).reader(reader()).writer(writer()).build();
//	}
//	// ジョブ
//	@Bean
//	public Job insertJob() {
//		return jobBuilderFactory.get("insertJob")
//			.incrementer(new RunIdIncrementer())
//			.listener(listener())
//			.flow(step1())
//			.end()
//			.build();
//	}
//}
