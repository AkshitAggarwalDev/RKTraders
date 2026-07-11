package RKTraders.web.Repositories;


import RKTraders.web.Model.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryRepo extends JpaRepository<Enquiry, Integer> {

}
