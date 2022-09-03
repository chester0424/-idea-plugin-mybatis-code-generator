package zone.pusu.mybatisCodeGenerator.ui;

import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import zone.pusu.mybatisCodeGenerator.common.MCGException;
import zone.pusu.mybatisCodeGenerator.setting.SettingCustomizeCfgCol;
import zone.pusu.mybatisCodeGenerator.setting.SettingCustomizeCfgColItem;
import zone.pusu.mybatisCodeGenerator.setting.SettingCustomizeCfgColStoreService;
import zone.pusu.mybatisCodeGenerator.setting.SettingCustomizeCfgColTypeEnum;
import zone.pusu.mybatisCodeGenerator.tool.JsonUtil;
import zone.pusu.mybatisCodeGenerator.tool.ObjectUtil;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class SettingCustomizeCfgColUI implements Configurable {
    private SettingCustomizeCfgCol settingCustomizeCfgCol = new SettingCustomizeCfgCol();

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Customize Config Column";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return initUI();
    }

    @Override
    public boolean isModified() {
        return !JsonUtil.toJson(settingCustomizeCfgCol).equals(JsonUtil.toJson(SettingCustomizeCfgColStoreService.getInstance().getState()));
    }

    @Override
    public void apply() throws ConfigurationException {
        SettingCustomizeCfgColStoreService.getInstance().loadState(settingCustomizeCfgCol);
    }

    private JComponent initUI() {
        // 数据初始化
        settingCustomizeCfgCol = ObjectUtil.clone(SettingCustomizeCfgColStoreService.getInstance().getState(), new TypeToken<SettingCustomizeCfgCol>() {
        });

        // 容器
        JPanel jPanelContainer = new JPanel();
        jPanelContainer.setLayout(new BorderLayout());

        JScrollPane jScrollPane = new JBScrollPane();
        jPanelContainer.add(jScrollPane, BorderLayout.CENTER);

        JTable jTable = new JBTable();
        jScrollPane.getViewport().add(jTable);
        TableModel tableModel = new TableModel() {
            @Override
            public int getRowCount() {
                return settingCustomizeCfgCol.getItems().size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Nls
            @Override
            public String getColumnName(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return "Name";
                    case 1:
                        return "Type";
                    case 2:
                        return "Options";
                    default:
                        throw new MCGException("Not Supported");
                }
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return settingCustomizeCfgCol.getItems().get(rowIndex).getName();
                    case 1:
                        return settingCustomizeCfgCol.getItems().get(rowIndex).getType();
                    case 2:
                        return settingCustomizeCfgCol.getItems().get(rowIndex).getOptions();
                    default:
                        throw new MCGException("Not Supported");
                }
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        settingCustomizeCfgCol.getItems().get(rowIndex).setName(aValue.toString());
                        break;
                    case 1:
                        settingCustomizeCfgCol.getItems().get(rowIndex).setType(aValue.toString());
                        break;
                    case 2:
                        settingCustomizeCfgCol.getItems().get(rowIndex).setOptions(aValue.toString());
                        break;
                    default:
                        throw new MCGException("Not Supported");
                }
            }

            @Override
            public void addTableModelListener(TableModelListener l) {

            }

            @Override
            public void removeTableModelListener(TableModelListener l) {

            }
        };
        jTable.setModel(tableModel);

        JComboBox comboBoxCustomizeCfgColType = new ComboBox();
        for (SettingCustomizeCfgColTypeEnum value : SettingCustomizeCfgColTypeEnum.values()) {
            comboBoxCustomizeCfgColType.addItem(value.name());
        }

        TableCellEditor tableCellEditorJdbcType = new DefaultCellEditor(comboBoxCustomizeCfgColType);
        jTable.getColumnModel().getColumn(1).setCellEditor(tableCellEditorJdbcType);

        JPanel jPanelRight = new JPanel();
        jPanelRight.setLayout(new VerticalFlowLayout());
        jPanelContainer.add(jPanelRight, BorderLayout.EAST);

        JButton jButtonAdd = new JButton("Add");
        jPanelRight.add(jButtonAdd);
        jButtonAdd.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Common.inputItemName("custom column name", "", new InputValidator() {
                    @Override
                    public boolean checkInput(@NlsSafe String inputString) {
                        return allowInput(inputString);
                    }

                    @Override
                    public boolean canClose(@NlsSafe String inputString) {
                        return allowInput(inputString);
                    }

                    boolean allowInput(String inputString) {
                        if (settingCustomizeCfgCol.getItems().stream().filter(i -> i.getName().equals(inputString)).count() > 0) {
                            return false;
                        }
                        return true;
                    }

                }, new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        SettingCustomizeCfgColItem item = new SettingCustomizeCfgColItem();
                        item.setName(s);
                        item.setType(SettingCustomizeCfgColTypeEnum.BOOLEAN.name());
                        item.setOptions("");
                        settingCustomizeCfgCol.getItems().add(item);

                        jTable.updateUI();
                    }
                });
            }
        });
        JButton jButtonDelete = new JButton(("Delete"));
        jPanelRight.add(jButtonDelete);
        jButtonDelete.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = jTable.getSelectedRow();
                if (selectedRow > -1) {
                    SettingCustomizeCfgColItem item = settingCustomizeCfgCol.getItems().get(selectedRow);
                    settingCustomizeCfgCol.getItems().remove(item);

                    jTable.updateUI();
                }
            }
        });
        return jPanelContainer;

    }

}
