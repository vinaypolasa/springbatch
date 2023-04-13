package com.pvk.batchdemo.config;

import com.pvk.batchdemo.entity.Uszip;
import com.pvk.batchdemo.repository.ZipRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class ZipBatchConfig {
  private JobBuilderFactory jobBuilderFactory;

 private StepBuilderFactory stepBuilderFactory;

 private ZipRepository zipRepository;

 @Bean
 public FlatFileItemReader<Uszip> reader(){
     FlatFileItemReader<Uszip> itemReader = new FlatFileItemReader();
     itemReader.setResource(new FileSystemResource("src/main/resources/uszips_1000.csv"));
     itemReader.setName("zipscsvreader");
     itemReader.setLinesToSkip(1);
     itemReader.setLineMapper(lineMapper());
     return itemReader;
 }

    private LineMapper<Uszip> lineMapper() {
        DefaultLineMapper<Uszip> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("zip","city","state","country","timezone");
        BeanWrapperFieldSetMapper<Uszip> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Uszip.class);
        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
    @Bean
    public UsZipProccessor proccessor(){
     return new UsZipProccessor();

    }

    @Bean
    public RepositoryItemWriter<Uszip> writer(){
     RepositoryItemWriter<Uszip> writer = new RepositoryItemWriter<>();
     writer.setRepository(zipRepository);
     writer.setMethodName("save");
     return writer;
    }
    @Bean
    public Step step1(){
    return  stepBuilderFactory.get("zipcsv-step1").<Uszip,Uszip>chunk(10)
             .reader(reader())
             .processor(proccessor())
             .writer(writer())
            .taskExecutor(taskExecutor())
             .build();
    }

    @Bean
    public Job runjob(){
     return jobBuilderFactory.get("importzips")
             .flow(step1())
             .end().build();

    }
    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return  asyncTaskExecutor;
    }
}
