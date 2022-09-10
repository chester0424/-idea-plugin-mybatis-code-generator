package zone.pusu.mybatisCodeGenerator.ui;

import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import zone.pusu.mybatisCodeGenerator.common.MCGException;
import zone.pusu.mybatisCodeGenerator.setting.SettingTypeMapping;
import zone.pusu.mybatisCodeGenerator.setting.SettingTypeMappingItem;
import zone.pusu.mybatisCodeGenerator.setting.SettingTypeMappingStoreService;
import zone.pusu.mybatisCodeGenerator.tool.JsonUtil;
import zone.pusu.mybatisCodeGenerator.tool.ObjectUtil;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 设置-类型映射
 */
public class SettingTypeMappingUI implements Configurable {

    private SettingTypeMapping settingTypeMapping = new SettingTypeMapping();

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Type Mapping";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return initUi();
    }

    @Override
    public boolean isModified() {
        return !JsonUtil.toJson(settingTypeMapping).equals(JsonUtil.toJson(SettingTypeMappingStoreService.getInstance().getState()));
    }

    @Override
    public void apply() throws ConfigurationException {
        SettingTypeMappingStoreService.getInstance().loadState(settingTypeMapping);
    }

    private JComponent initUi() {
        // 数据初始化
        settingTypeMapping = ObjectUtil.clone(SettingTypeMappingStoreService.getInstance().getState(), new TypeToken<SettingTypeMapping>() {
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
                return settingTypeMapping.getItems().size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Nls
            @Override
            public String getColumnName(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return "JavaType";
                    case 1:
                        return "JdbcType";
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
                if (columnIndex == 0) {
                    return settingTypeMapping.getItems().get(rowIndex).getJavaType();
                } else {
                    return settingTypeMapping.getItems().get(rowIndex).getJdbcType();
                }
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                String val = aValue.toString();
                if (columnIndex == 0) {
                    settingTypeMapping.getItems().get(rowIndex).setJavaType(val);
                } else {
                    settingTypeMapping.getItems().get(rowIndex).setJdbcType(val);
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


        JPanel jPanelRight = new JPanel();
        jPanelRight.setLayout(new VerticalFlowLayout());
        jPanelContainer.add(jPanelRight, BorderLayout.EAST);

        JButton jButtonAdd = new JButton("Add");
        jPanelRight.add(jButtonAdd);
        jButtonAdd.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingTypeMappingItem item = new SettingTypeMappingItem();
                item.setJdbcType("");
                item.setJdbcType("");
                settingTypeMapping.getItems().add(item);

                jTable.updateUI();
            }
        });
        JButton jButtonDelete = new JButton(("Delete"));
        jPanelRight.add(jButtonDelete);
        jButtonDelete.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = jTable.getSelectedRow();
                if (selectedRow > -1) {
                    SettingTypeMappingItem item = settingTypeMapping.getItems().get(selectedRow);
                    settingTypeMapping.getItems().remove(item);

                    jTable.updateUI();
                }
            }
        });
        JButton jButtonReset = new JButton(("Reset"));
        jPanelRight.add(jButtonReset);
        jButtonReset.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure to reset?", "Notice", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    settingTypeMapping.setItems(SettingTypeMappingStoreService.getInstance().getState().getDefault());
                    jTable.updateUI();
                }
            }
        });
        return jPanelContainer;


    }
}
