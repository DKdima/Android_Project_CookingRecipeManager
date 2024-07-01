package com.example.aplicacion_dima;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public class CS_DiasRestantes {

    public static long calcularDiasFaltantes() {
        // Obtén la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Crea la fecha del último día de febrero de 2024
        LocalDate ultimoDiaFebrero2024 = LocalDate.of(2024, 6, 1);

        // Calcula los días que faltan
        return ChronoUnit.DAYS.between(fechaActual, ultimoDiaFebrero2024);
    }
}
