package integration.library.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Book {

    String title;

    DamageLevel damageLevel;

    double worth;

    boolean isDamaged;

}
