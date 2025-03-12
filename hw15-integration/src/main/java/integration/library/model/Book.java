package integration.library.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Book {

    private String title;

    private DamageLevel damageLevel;

    private double worth;

    private boolean isDamaged;

}
