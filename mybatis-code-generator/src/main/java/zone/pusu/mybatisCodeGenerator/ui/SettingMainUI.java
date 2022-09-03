package zone.pusu.mybatisCodeGenerator.ui;

import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zone.pusu.mybatisCodeGenerator.setting.SettingMain;
import zone.pusu.mybatisCodeGenerator.setting.SettingMainStoreService;
import zone.pusu.mybatisCodeGenerator.tool.JsonUtil;
import zone.pusu.mybatisCodeGenerator.tool.ObjectUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * 设置-主界面
 */
public class SettingMainUI implements Configurable, Configurable.Composite {

    SettingMain settingMain = new SettingMain();
    private boolean modifiedFlag = false;
    private String authorName = "";

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "MyBatis CodeGenerator FWD DIR";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return getMainUI();
    }

    @Override
    public boolean isModified() {
        return !JsonUtil.toJson(settingMain).equals(JsonUtil.toJson(SettingMainStoreService.getInstance().getState()));
    }

    @Override
    public void apply() throws ConfigurationException {
        SettingMainStoreService.getInstance().loadState(settingMain);
    }

    @Override
    public @NotNull Configurable[] getConfigurables() {
        Configurable[] configurables = new Configurable[]{
                new SettingTemplateUI(),
                new SettingTypeMappingUI(),
                new SettingCustomizeCfgColUI()
        };
        return configurables;
    }

    private JPanel getMainUI() {
        // 初始数据
        settingMain = ObjectUtil.clone(SettingMainStoreService.getInstance().getState(), new TypeToken<SettingMain>() {
        });

        // 构造UI视图
        JPanel jPanelContainer = new JPanel();
        jPanelContainer.setLayout(new VerticalFlowLayout(VerticalFlowLayout.LEFT));

        // 用户信息
        JPanel jPanelAuthor = new JPanel();
        jPanelAuthor.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelAuthor.add(new JLabel("Author    Name:"));
        JTextField textFieldAuthorName = new JTextField();
        textFieldAuthorName.setPreferredSize(new Dimension(400, 30));
        textFieldAuthorName.setText(settingMain.getAuthorName());
        textFieldAuthorName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChanged();
            }

            private void onChanged() {
                String text = textFieldAuthorName.getText();
                settingMain.setAuthorName(text);
            }
        });
        jPanelAuthor.add(textFieldAuthorName);
        jPanelContainer.add(jPanelAuthor);

        // 对象配置文件存储位置
        JPanel jPanelConfigFileSavePath = new JPanel();
        jPanelConfigFileSavePath.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelConfigFileSavePath.add(new JLabel("Config File Path:"));
        JTextField textFieldConfigFileSavePath = new JTextField();
        textFieldConfigFileSavePath.setPreferredSize(new Dimension(400, 30));
        textFieldConfigFileSavePath.setText(settingMain.getConfigFileSavePath());
        textFieldConfigFileSavePath.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChanged();
            }

            private void onChanged() {
                String text = textFieldConfigFileSavePath.getText();
                settingMain.setConfigFileSavePath(text);
            }
        });
        jPanelConfigFileSavePath.add(textFieldConfigFileSavePath);
        jPanelContainer.add(jPanelConfigFileSavePath);

        return jPanelContainer;
    }
}
