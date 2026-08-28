package RKTraders.web.Modules.Enquiry;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryRepo extends JpaRepository<EnquiryEntity, Integer> {

}
