package zw.co.malvern.repository.api;

import zw.co.malvern.util.dto.ProcedureDto;

public interface CustomerRepository {

    void deleteStoredProcedures();

    void createCustomerRetreiveProcedure();

    void createFindCustomerByAgeProcedure();

    void createFindCustomerAgeByNameProcedure();

    void createSumAllPointsProcedure();

    void findCustomerByName();

    void findCustomerBySurname();

    void alterCustomerRetreiveProcedure();

    ProcedureDto viewStoredProceduresInformation();
}
