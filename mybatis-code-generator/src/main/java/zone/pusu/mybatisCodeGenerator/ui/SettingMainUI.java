package zone.pusu.mybatisCodeGenerator.ui;

import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zone.pusu.mybatisCodeGenerator.config.ConfigManager;
import zone.pusu.mybatisCodeGenerator.setting.SettingMain;
import zone.pusu.mybatisCodeGenerator.setting.SettingMainStoreService;
import zone.pusu.mybatisCodeGenerator.tool.JsonUtil;
import zone.pusu.mybatisCodeGenerator.tool.ObjectUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        return "MyBatis Forward CodeGenerator";
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
    public void apply() {
        SettingMainStoreService.getInstance().loadState(settingMain);
    }

    @Override
    public @NotNull Configurable[] getConfigurables() {
        return new Configurable[]{
                new SettingHelpUI()
        };
    }

    private JPanel getMainUI() {
        // 初始数据
        settingMain = ObjectUtil.clone(SettingMainStoreService.getInstance().getState(), new TypeToken<SettingMain>() {
        });

        // 构造UI视图
        JPanel jPanelContainer = new JPanel();
        jPanelContainer.setLayout(new VerticalFlowLayout(VerticalFlowLayout.LEFT));

        JLabel jLabelLogon = new JLabel();
        jLabelLogon.setText(" MEET ME MEET BEAUTIFUL, NICE DAY");
        Font font = new Font("宋体", Font.BOLD, 24);
        jLabelLogon.setPreferredSize(new Dimension(200, 80));
        jLabelLogon.setFont(font);
        jPanelContainer.add(jLabelLogon);

        // 用户信息
        JPanel jPanelAuthor = new JPanel();
        jPanelAuthor.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelAuthor.add(new JLabel("Author    Name:"));
        JTextField textFieldAuthorName = new JTextField();
        textFieldAuthorName.setPreferredSize(new Dimension(400, 30));
        textFieldAuthorName.setText(settingMain.getAuthor());
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
                settingMain.setAuthor(text);
            }
        });
        jPanelAuthor.add(textFieldAuthorName);
        jPanelContainer.add(jPanelAuthor);

        // 保存默认信息到本地
        JPanel jPanelConfigFileSavePath = new JPanel();
        jPanelConfigFileSavePath.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelConfigFileSavePath.setBorder(new EmptyBorder(200, 0, 0, 0));
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/img/save.png"));
        icon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        JButton jButtonSaveDefaultToLocal = new JButton("Save DEFAULT CONFIG to the local project", icon);

        jButtonSaveDefaultToLocal.addActionListener(e -> {
            saveConfigToLocal();
        });
        jPanelConfigFileSavePath.add(jButtonSaveDefaultToLocal);
        jButtonSaveDefaultToLocal.setVisible(true);
        jPanelContainer.add(jPanelConfigFileSavePath);

        return jPanelContainer;
    }

    private void saveConfigToLocal() {
        try {
            ConfigManager.saveDefaultConfigToLocal();

            Messages.showInfoMessage("Successfully Saved", "Success");
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), "ERROR");
        }
    }
}
