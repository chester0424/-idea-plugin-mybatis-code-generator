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
import zone.pusu.mybatisCodeGenerator.setting.SettingExtendCfgCol;
import zone.pusu.mybatisCodeGenerator.setting.SettingExtendCfgColItem;
import zone.pusu.mybatisCodeGenerator.setting.SettingExtendCfgColStoreService;
import zone.pusu.mybatisCodeGenerator.setting.SettingExtendCfgColTypeEnum;
import zone.pusu.mybatisCodeGenerator.tool.JsonUtil;
import zone.pusu.mybatisCodeGenerator.tool.ObjectUtil;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class SettingExtendCfgColUI implements Configurable {
    private SettingExtendCfgCol settingExtendCfgCol = new SettingExtendCfgCol();

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Extend Config Column";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return initUI();
    }

    @Override
    public boolean isModified() {
        return !JsonUtil.toJson(settingExtendCfgCol).equals(JsonUtil.toJson(SettingExtendCfgColStoreService.getInstance().getState()));
    }

    @Override
    public void apply() {
        SettingExtendCfgColStoreService.getInstance().loadState(settingExtendCfgCol);
    }

    private JComponent initUI() {
        // 数据初始化
        settingExtendCfgCol = ObjectUtil.clone(SettingExtendCfgColStoreService.getInstance().getState(), new TypeToken<SettingExtendCfgCol>() {
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
                return settingExtendCfgCol.getItems().size();
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
                        return settingExtendCfgCol.getItems().get(rowIndex).getName();
                    case 1:
                        return settingExtendCfgCol.getItems().get(rowIndex).getType();
                    case 2:
                        return settingExtendCfgCol.getItems().get(rowIndex).getOptions();
                    default:
                        throw new MCGException("Not Supported");
                }
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        settingExtendCfgCol.getItems().get(rowIndex).setName(aValue.toString());
                        break;
                    case 1:
                        settingExtendCfgCol.getItems().get(rowIndex).setType(aValue.toString());
                        break;
                    case 2:
                        settingExtendCfgCol.getItems().get(rowIndex).setOptions(aValue.toString());
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
        for (SettingExtendCfgColTypeEnum value : SettingExtendCfgColTypeEnum.values()) {
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

                Common.inputDialog("Extend Column Name", "", new InputValidator() {
                    @Override
                    public boolean checkInput(@NlsSafe String inputString) {
                        return allowInput(inputString);
                    }

                    @Override
                    public boolean canClose(@NlsSafe String inputString) {
                        return allowInput(inputString);
                    }

                    boolean allowInput(String inputString) {
                        if (settingExtendCfgCol.getItems().stream().filter(i -> i.getName().equals(inputString)).count() > 0) {
                            return false;
                        }
                        return true;
                    }

                }, new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        SettingExtendCfgColItem item = new SettingExtendCfgColItem();
                        item.setName(s);
                        item.setType(SettingExtendCfgColTypeEnum.BOOLEAN.name());
                        item.setOptions("");
                        settingExtendCfgCol.getItems().add(item);

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
                    SettingExtendCfgColItem item = settingExtendCfgCol.getItems().get(selectedRow);
                    settingExtendCfgCol.getItems().remove(item);

                    jTable.updateUI();
                }
            }
        });
        return jPanelContainer;

    }

}
