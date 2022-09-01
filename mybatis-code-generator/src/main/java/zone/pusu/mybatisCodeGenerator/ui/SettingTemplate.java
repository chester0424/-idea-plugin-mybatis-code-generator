package zone.pusu.mybatisCodeGenerator.ui;

import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.Nullable;
import zone.pusu.mybatisCodeGenerator.setting.SettingTemplateItem;
import zone.pusu.mybatisCodeGenerator.setting.SettingTemplateStoreService;
import zone.pusu.mybatisCodeGenerator.tool.JsonUtil;
import zone.pusu.mybatisCodeGenerator.tool.ObjectUtil;
import zone.pusu.mybatisCodeGenerator.tool.StringUtil;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SettingTemplate implements Configurable {
    List<SettingTemplateItem> itemList = new ArrayList<>();
    // 模板列表
//    private DefaultListModel dlm = new DefaultListModel();

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Template";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return getMainUI();
    }

    @Override
    public boolean isModified() {
        return !JsonUtil.toJson(itemList).equals(JsonUtil.toJson(SettingTemplateStoreService.getInstance().getState().getItemList()));
    }

    @Override
    public void apply() throws ConfigurationException {
        SettingTemplateStoreService.getInstance().getState().setItemList(itemList);
    }

    private JPanel getMainUI() {
        itemList = ObjectUtil.clone(SettingTemplateStoreService.getInstance().getState().getItemList(), new TypeToken<List<SettingTemplateItem>>() {
        });

        // 容器
        JPanel jPanelContainer = new JPanel();
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
        jSplitPaneMain.setResizeWeight(0.2);
        jSplitPaneMain.setDividerLocation(.2);
        jPanelContainer.add(jSplitPaneMain, BorderLayout.CENTER);
        // 模板列表
        JList jListTemplateName = new JList();
        jSplitPaneMain.setLeftComponent(jListTemplateName);
        // 编辑器
        JTextArea jTextAreaEditor = new JTextArea();
        jSplitPaneMain.setRightComponent(jTextAreaEditor);

        jButtonAdd.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputItemName("Template Name", "", new InputValidator() {
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
                }, new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        SettingTemplateItem item = new SettingTemplateItem();
                        item.setName(s);
                        item.setContent("");

                        itemList.add(item);
                        refreshModel(jListTemplateName);
                    }
                });
            }
        });

        jButtonDelete.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String templateName = jListTemplateName.getSelectedValue().toString();
                SettingTemplateItem settingTemplateItem = itemList.stream().filter(i -> i.getName().equals(templateName)).findFirst().get();
                itemList.remove(settingTemplateItem);
                refreshModel(jListTemplateName);
            }
        });
//        DefaultListModel
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
        jListTemplateName.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String templateName = jListTemplateName.getSelectedValue().toString();
                SettingTemplateItem item = itemList.stream().filter(i -> i.getName().equals(templateName)).findFirst().get();
                jTextAreaEditor.setText(item.getContent());
            }
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
                String templateName = jListTemplateName.getSelectedValue().toString();
                String templateContent = jTextAreaEditor.getText();

                Optional<SettingTemplateItem> optional = itemList.stream().filter(i -> i.getName().equals(templateName)).findFirst();
                if (optional.isPresent()) {
                    optional.get().setContent(templateContent);
                }
            }
        });

        return jPanelContainer;
    }

    private void refreshModel(JList jListTemplateName){
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
    }
    private void inputItemName(String message, String initValue, InputValidator validator, Consumer<String> consumer) {
        String value = Messages.showInputDialog(message, "Input " + message, Messages.getQuestionIcon(), initValue, validator);
        if (StringUtil.isNullOrEmpty(value)) {
            return;
        }
        consumer.accept(value);
    }
}
