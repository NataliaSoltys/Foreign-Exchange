package com.ratesservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratesservice.model.dto.CurrencyResponseDto;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyResponseDtoTest {

    @Test
    void testDeserialize() throws IOException {
        String json = "[ {\n" +
                "  \"table\" : \"C\",\n" +
                "  \"no\" : \"041/C/NBP/2025\",\n" +
                "  \"tradingDate\" : \"2025-02-27\",\n" +
                "  \"effectiveDate\" : \"2025-02-28\",\n" +
                "  \"rates\" : [ {\n" +
                "    \"currency\" : \"dolar amerykański\",\n" +
                "    \"code\" : \"USD\",\n" +
                "    \"bid\" : 3.9406,\n" +
                "    \"ask\" : 4.0202\n" +
                "  } ]\n" +
                "} ]";

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyResponseDto[] response = objectMapper.readValue(json, CurrencyResponseDto[].class);

        // Assertions
        assertNotNull(response);
        assertEquals("C", response[0].getTable());
        assertEquals("041/C/NBP/2025", response[0].getNo());
        assertEquals(1, response[0].getRates().size());
        assertEquals("USD", response[0].getRates().get(0).getCode());
        assertEquals("dolar amerykański", response[0].getRates().get(0).getCurrencyName());
    }
}
