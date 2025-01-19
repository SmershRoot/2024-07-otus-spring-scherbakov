package ru.otus.hw.configuration.h2.to.mongo;

//@Configuration
public class BatchConfigurationH2ToMongo {
//
//    private final JobRepository jobRepository;
//
//    private final PlatformTransactionManager platformTransactionManager;
//
//    private final AuthorMongoRepository mongoAuthorRepository;
//
//    public BatchConfigurationH2ToMongo(
//            JobRepository jobRepository,
//            PlatformTransactionManager platformTransactionManager,
//            AuthorMongoRepository mongoAuthorRepository
//    ) {
//        this.jobRepository = jobRepository;
//        this.platformTransactionManager = platformTransactionManager;
//        this.mongoAuthorRepository = mongoAuthorRepository;
//    }
//
//    @Bean
//    public Job migrateJobH2ToMongo(Step migrateAuthorStepH2ToMongo) {
//        System.out.println("migrateJobH2ToMongo");
//        return new JobBuilder("migrateJobH2ToMongo", jobRepository)
//                .incrementer(new RunIdIncrementer())
//                .start(migrateAuthorStepH2ToMongo)
//                .build();
//    }
//
//    @Bean
//    public Step migrateAuthorStepH2ToMongo(
//            RepositoryItemReader<AuthorMongo> authorReaderH2ToMongo,
//            ItemWriter<AuthorMongo> authorWriterH2ToMongo,
//            ItemProcessor<AuthorMongo, AuthorMongo> authorProcessorH2ToMongo) {
//        System.out.println("migrateHTwoAuthorStepH2ToMongo");
//        return new StepBuilder("migrateAuthorStepH2ToMongo", jobRepository)
//                .<AuthorMongo, AuthorMongo>chunk(3, platformTransactionManager)
//                .reader(authorReaderH2ToMongo)
//                .processor(authorProcessorH2ToMongo)
//                .writer(authorWriterH2ToMongo)
//                .taskExecutor(new SimpleAsyncTaskExecutor())
//                .build();
//    }
//
//    @Bean
//    public RepositoryItemReader<AuthorMongo> authorReaderH2ToMongo() {
//        System.out.println("authorReaderH2ToMongo");
//        return new RepositoryItemReaderBuilder<AuthorMongo>()
//                .name("authorReaderH2ToMongo")
//                .repository(mongoAuthorRepository)
//                .methodName("findAll")
//                .sorts(new HashMap<>())
//                .build();
//    }
//
//    @Bean
//    public AuthorProcessor authorProcessorH2ToMongo() {
//        System.out.println("authorProcessorH2ToMongo");
//        return new AuthorProcessor();
//    }
//
//    @Bean
//    public ItemWriter<AuthorMongo> authorWriterH2ToMongo() {
//        return new ItemWriter<AuthorMongo>() {
//            @Override
//            public void write(Chunk<? extends AuthorMongo> chunk) throws Exception {
//                System.out.println("authorWriterH2ToMongo: " + chunk.size());
//            }
//        };
//    }

}
