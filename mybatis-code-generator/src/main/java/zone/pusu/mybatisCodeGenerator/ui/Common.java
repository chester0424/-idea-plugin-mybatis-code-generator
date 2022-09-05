package zone.pusu.mybatisCodeGenerator.ui;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import zone.pusu.mybatisCodeGenerator.tool.StringUtil;

import java.awt.*;
import java.util.function.Consumer;

public class Common {

    /**
     * 弹出输入框
     *
     * @param message
     * @param initValue
     * @param validator
     * @param consumer
     */
    public static void inputDialog(String message, String initValue, InputValidator validator, Consumer<String> consumer) {
        String value = Messages.showInputDialog(message, "Input " + message, Messages.getQuestionIcon(), initValue, validator);
        if (StringUtil.isNullOrEmpty(value)) {
            return;
        }
        consumer.accept(value);
    }

    /**
     * 设置显示屏居中
     *
     * @param component
     */
    public static void setCenterLocation(Component component) {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        component.setLocation((screenWidth - component.getWidth()) / 2, (screenHeight - component.getHeight()) / 2);
    }
}
