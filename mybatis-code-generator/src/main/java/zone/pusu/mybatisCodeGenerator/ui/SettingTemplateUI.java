package zone.pusu.mybatisCodeGenerator.ui;

import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;
import zone.pusu.mybatisCodeGenerator.setting.SettingTemplateItem;
import zone.pusu.mybatisCodeGenerator.setting.SettingTemplateStoreService;
import zone.pusu.mybatisCodeGenerator.tool.JsonUtil;
import zone.pusu.mybatisCodeGenerator.tool.ObjectUtil;
import zone.pusu.mybatisCodeGenerator.tool.StringUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 设置-模板
 */
public class SettingTemplateUI implements Configurable {
    List<SettingTemplateItem> itemList = new ArrayList<>();

    //region Configurable

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Template";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return initUI();
    }

    @Override
    public boolean isModified() {
        return !JsonUtil.toJson(itemList).equals(JsonUtil.toJson(SettingTemplateStoreService.getInstance().getState().getItems()));
    }

    @Override
    public void apply() {
        SettingTemplateStoreService.getInstance().getState().setItems(itemList);
    }

    //endregion

    private JComponent initUI() {
        itemList = ObjectUtil.clone(SettingTemplateStoreService.getInstance().getState().getItems(), new TypeToken<List<SettingTemplateItem>>() {
        });

        // 容器
        JPanel jPanelContainer = new JPanel();
        jPanelContainer.setAutoscrolls(false);
        jPanelContainer.setLayout(new BorderLayout());
        // 头部布局
        JPanel jPanelHead = new JPanel();
        jPanelHead.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelContainer.add(jPanelHead, BorderLayout.NORTH);
        // 添加按钮
        JButton jButtonAdd = new JButton("Add");
        jPanelHead.add(jButtonAdd);
        // 删除按钮
        JButton jButtonDelete = new JButton("Delete");
        jPanelHead.add(jButtonDelete);

        // 主要区域
        JSplitPane jSplitPaneMain = new JSplitPane();
        jSplitPaneMain.setResizeWeight(0.3);
        jSplitPaneMain.setDividerLocation(.3); //分割比例
        jPanelContainer.add(jSplitPaneMain, BorderLayout.CENTER);
        // 模板列表
        JList jListTemplateName = new JBList();
        jListTemplateName.setBackground(new Color(69, 73, 74));
        jSplitPaneMain.setLeftComponent(jListTemplateName);

        // 编辑器
        JTextArea jTextAreaEditor = new JTextArea();
        jTextAreaEditor.setAutoscrolls(true);
        JScrollPane jScrollPane = new JBScrollPane(jTextAreaEditor);
        jScrollPane.setPreferredSize(new Dimension(200, 200));
        jSplitPaneMain.setRightComponent(jScrollPane);

        jButtonAdd.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Common.inputDialog("Template Name", "", new InputValidator() {
                    @Override
                    public boolean checkInput(@NlsSafe String inputString) {
                        return allowInput(inputString);
                    }

                    @Override
                    public boolean canClose(@NlsSafe String inputString) {
                        return allowInput(inputString);
                    }

                    boolean allowInput(String inputString) {
                        return !(itemList.stream().filter(i -> i.getName().equals(inputString)).count() > 0);
                    }
                }, s -> {
                    SettingTemplateItem item = new SettingTemplateItem();
                    item.setName(s);
                    item.setContent("");
                    itemList.add(item);
                    jListTemplateName.updateUI();
                    jListTemplateName.setSelectedValue(s, true);
                });
            }
        });

        jButtonDelete.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String templateName = jListTemplateName.getSelectedValue().toString();
                SettingTemplateItem settingTemplateItem = itemList.stream().filter(i -> i.getName().equals(templateName)).findFirst().get();
                itemList.remove(settingTemplateItem);
                jListTemplateName.updateUI();
            }
        });

        jListTemplateName.setModel(new ListModel() {
            @Override
            public int getSize() {
                return itemList.size();
            }

            @Override
            public Object getElementAt(int index) {
                return itemList.get(index).getName();
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });
        jListTemplateName.addListSelectionListener(e -> {
            String templateName = jListTemplateName.getSelectedValue().toString();
            SettingTemplateItem item = itemList.stream().filter(i -> i.getName().equals(templateName)).findFirst().get();
            jTextAreaEditor.setText(item.getContent());
        });
        jTextAreaEditor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onTemplateContentChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onTemplateContentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onTemplateContentChanged();
            }

            private void onTemplateContentChanged() {
                if (jListTemplateName.getSelectedValue() != null) {
                    String templateName = jListTemplateName.getSelectedValue().toString();
                    String templateContent = jTextAreaEditor.getText();
                    if (!StringUtil.isNullOrEmpty(templateName)) {
                        Optional<SettingTemplateItem> optional = itemList.stream().filter(i -> i.getName().equals(templateName)).findFirst();
                        optional.ifPresent(settingTemplateItem -> settingTemplateItem.setContent(templateContent));
                    }
                } else {
                    Messages.showInfoMessage("Add a template first", "Info");
                }
            }
        });


        return jPanelContainer;
    }
}
