package models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;

@Data
@AllArgsConstructor
public class LineStatus implements Comparable<LineStatus>{
    private String status;
    private String arquivo;
    private String task;

    public int compareTo(LineStatus o) {
        return Comparator.comparing(LineStatus::getStatus)
                .thenComparing(LineStatus::getArquivo)
                .compare(this, o);
    }
}
