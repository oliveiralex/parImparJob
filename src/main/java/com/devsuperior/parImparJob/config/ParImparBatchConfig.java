package com.devsuperior.parImparJob.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ParImparBatchConfig {
	
	@Bean
	public Job imprimeParImparJob(JobRepository jobRepository, Step step) {
		return new JobBuilder("imprimeParImparJob", jobRepository)
				.start(step)
				.incrementer(new RunIdIncrementer())
				.build();
	}
	
	@Bean
	public Step imprimeParImparStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("imprimeParImparStep", jobRepository)
				.<Integer,String>chunk(1, transactionManager)
				.reader(contaAteDezReader())
				.processor(parOuImparProcessor())
				.writer(imprimeWriter())
				.build();
	}

	public IteratorItemReader<Integer> contaAteDezReader() {
		List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		return new IteratorItemReader<Integer>(values.iterator());
	}
	
	public FunctionItemProcessor<Integer, String> parOuImparProcessor() {
		return new FunctionItemProcessor<Integer, String>
		(item -> item % 2 == 0 ? String.format("Item %s é par", item) : String.format("Item %s é impar", item));
	}
	
	public ItemWriter<String> imprimeWriter() {
		return itens -> itens.forEach(System.out::println);
	}

}
