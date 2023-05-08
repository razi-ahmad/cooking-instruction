package nl.abnamro.recipe.eception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AbnamroBaseException extends RuntimeException {


    public AbnamroBaseException(String message) {
        super(message);
    }
}
