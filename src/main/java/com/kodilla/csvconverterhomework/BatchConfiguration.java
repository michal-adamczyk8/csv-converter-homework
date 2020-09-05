package com.kodilla.csvconverterhomework;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.ExtractorLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;

    public BatchConfiguration(StepBuilderFactory stepBuilderFactory, JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    FlatFileItemReader<Person1> flatFileItemReader() {
        FlatFileItemReader reader = new FlatFileItemReader();
        reader.setResource(new ClassPathResource("input.csv"));

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(";");
        tokenizer.setNames("firstName", "lastName", "birth");

        BeanWrapperFieldSetMapper mapper = new BeanWrapperFieldSetMapper();
        mapper.setTargetType(Person1.class);

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    PersonProcessor processor() {
        return new PersonProcessor();
    }

    @Bean
    FlatFileItemWriter flatFileItemWriter() {
        BeanWrapperFieldExtractor extractor = new BeanWrapperFieldExtractor();
        extractor.setNames(new String[]{"firstName", "lastName", "age"});

        DelimitedLineAggregator aggregator = new DelimitedLineAggregator();
        aggregator.setDelimiter(";");
        aggregator.setFieldExtractor(extractor);

        FlatFileItemWriter writer = new FlatFileItemWriter();
        writer.setResource(new FileSystemResource("output.csv"));
        writer.setLineAggregator(aggregator);
        writer.setShouldDeleteIfExists(true);
        return writer;
    }

    @Bean
    Step ageCalc(
            ItemReader<Person1> reader,
            ItemWriter<Person2> writer,
            ItemProcessor<Person1, Person2> processor
    ) {
        return stepBuilderFactory.get("ageCalc")
                .<Person1, Person2>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    Job ageCalcJob(Step ageCalc) {
        return jobBuilderFactory.get("ageCalcJob")
                .incrementer(new RunIdIncrementer())
                .flow(ageCalc)
                .end()
                .build();
    }


}
