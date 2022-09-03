package zone.pusu.mybatisCodeGenerator.ui;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import zone.pusu.mybatisCodeGenerator.tool.StringUtil;

import java.util.function.Consumer;

public class Common {

    public static void inputItemName(String message, String initValue, InputValidator validator, Consumer<String> consumer) {
        String value = Messages.showInputDialog(message, "Input " + message, Messages.getQuestionIcon(), initValue, validator);
        if (StringUtil.isNullOrEmpty(value)) {
            return;
        }
        consumer.accept(value);
    }
}
