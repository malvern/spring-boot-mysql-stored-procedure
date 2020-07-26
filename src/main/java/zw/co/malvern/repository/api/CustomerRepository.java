package zw.co.malvern.repository.api;

import zw.co.malvern.util.dto.ProcedureDto;

public interface CustomerRepository {



    void createCustomerRetreiveProcedure();
    void createFindCustomerByAgeProcedure();
    void createSumAllPointsProcedure();
    void findCustomerBySurname();
    void createFindCustomerAgeByNameProcedure();
    void deleteStoredProcedures(String procedureName);

    Long findCustomerByAge(String name);



    void alterCustomerRetreiveProcedure();




    ProcedureDto viewStoredProceduresInformation();
}
