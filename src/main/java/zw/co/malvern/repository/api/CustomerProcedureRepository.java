package zw.co.malvern.repository.api;

import zw.co.malvern.util.dto.ProcedureDto;


public interface CustomerProcedureRepository {

    void createCustomerRetreiveProcedure();

    void createFindCustomerByAgeProcedure();

    void createSumAllPointsProcedure();

    void findCustomerBySurname();

    void createFindCustomerAgeByNameProcedure();

    void deleteStoredProcedures(String procedureName);

    void alterCustomerRetreiveProcedure();

    ProcedureDto viewStoredProceduresInformation();

}
