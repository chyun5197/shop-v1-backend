//package project.shopclone.domain.chat.repository;
//
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//import project.shopclone.domain.chat.document.Chat;
//
//import java.util.List;
//
//public interface ChatRepository extends MongoRepository<Chat, String> {
//    @Query("{name: '?0'}")
//    Chat findByName(String name);
//
//    @Query(value = "{name: '?0'}")
//    List<Chat> findAllByName(String name);
//
//    public long count();
//}
