package zw.co.malvern.util.converter;

import zw.co.malvern.util.dto.ProcedureDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultConverter {

    private ResultConverter() {
    }

    public static ProcedureDto convertMysqlResultToProcedureDto(ResultSet resultSet) throws SQLException {
        final ProcedureDto procedureDto = new ProcedureDto();
        procedureDto.setName(resultSet.getString(1));
        procedureDto.setComment(resultSet.getString(27));
        procedureDto.setDateCreated(resultSet.getString(25));
        procedureDto.setDefiner(resultSet.getString(28));
        procedureDto.setDeterminstic(resultSet.getString(20));
        procedureDto.setLastAltered(resultSet.getString(24));
        procedureDto.setRoutineBodyDefination(resultSet.getString(16));
        procedureDto.setRoutineBodyLanguage(resultSet.getString(15));
        procedureDto.setRoutineSchema(resultSet.getString(3));
        procedureDto.setSecurityType(resultSet.getString(22));
        procedureDto.setSqlMode(resultSet.getString(23));
       return procedureDto;
    }
}
