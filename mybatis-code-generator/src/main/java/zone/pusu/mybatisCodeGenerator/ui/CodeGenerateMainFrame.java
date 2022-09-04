package zone.pusu.mybatisCodeGenerator.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nls;
import zone.pusu.mybatisCodeGenerator.define.GenerateMybatisConfigClass;
import zone.pusu.mybatisCodeGenerator.define.GenerateMybatisConfigField;
import zone.pusu.mybatisCodeGenerator.setting.SettingTemplateItem;
import zone.pusu.mybatisCodeGenerator.setting.SettingTemplateStoreService;
import zone.pusu.mybatisCodeGenerator.tool.JdbcTypeUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class CodeGenerateMainFrame extends JFrame {
    // 操作类型申请
    public static int Operate_Save = 1;
    public static int Operate_Generate = 2;

    // 组件
    JPanel jPanelHead;
    JTextField textFieldTableName;
    JPanel jPanelCenter;
    JBTable table;
    JScrollPane jScrollPane;
    JPanel jPanelFoot;
    JPanel jPanelSelectFile;
    JPanel jPanelOperate;
    JButton jButtonSave;
    JButton jButtonGenerate;
    JButton jButtonCancel;

    // 代码生成配置信息
    GenerateMybatisConfigClass generateMybatisConfigClass;
    // 事件声明
    private ArrayList<CodeGenerateMainFrameOperateEvent> eventListenerList = new ArrayList<CodeGenerateMainFrameOperateEvent>();
    // 记录模板是否需要生成代码
    private Map<SettingTemplateItem, Boolean> templateSelectedState = new LinkedHashMap<>();

    public CodeGenerateMainFrame(GenerateMybatisConfigClass generateMybatisConfigClass) {
        // 初始化数据
        this.generateMybatisConfigClass = generateMybatisConfigClass;
        // 读取配置文件
        for (SettingTemplateItem item : SettingTemplateStoreService.getInstance().getState().getItems()) {
            templateSelectedState.put(item, false);
        }


        jPanelHead = new JPanel();
        jPanelHead.setBorder(new EmptyBorder(20, 20, 0, 20));
        jPanelHead.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(jPanelHead, BorderLayout.NORTH);
        JLabel jLabelTableName = new JLabel("TableName:");
        textFieldTableName = new JTextField();
        textFieldTableName.setText(generateMybatisConfigClass.getTableName());
        textFieldTableName.setPreferredSize(new Dimension(400, 30));
        textFieldTableName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                triggerChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                triggerChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                triggerChanged();
            }

            private void triggerChanged() {
                String text = textFieldTableName.getText();
                generateMybatisConfigClass.setTableName(text);
            }
        });
        jPanelHead.add(jLabelTableName);
        jPanelHead.add(textFieldTableName);

        // 数据部分
        jPanelCenter = new JPanel();
        jPanelCenter.setLayout(new BorderLayout());
        this.add(jPanelCenter, BorderLayout.CENTER);

        table = new JBTable();
        table.setBorder(new LineBorder(Color.GRAY, 1));
        table.setRowHeight(32);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(1, 50));
        // 滚动
        jScrollPane = new JBScrollPane(table);
        jScrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        jPanelCenter.add(jScrollPane);

        table.setModel(new MyTableModel(generateMybatisConfigClass));

        JComboBox comboBoxJdbcType = new ComboBox();

        comboBoxJdbcType.addItem("");
        for (String allJdbcType : JdbcTypeUtil.getAllJdbcTypes()) {
            comboBoxJdbcType.addItem(allJdbcType);
        }

        TableCellEditor tableCellEditorJdbcType = new DefaultCellEditor(comboBoxJdbcType);
        table.getColumnModel().getColumn(4).setCellEditor(tableCellEditorJdbcType);


        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                int columnIndex = table.getSelectedColumn();
                if (columnIndex == 3) {
                    Boolean selected = (Boolean) table.getValueAt(rowIndex, 3);
                    if (selected) {
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (i != rowIndex) {
                                boolean val = (Boolean) table.getValueAt(i, 3);
                                if (val) {
                                    table.setValueAt(false, i, 3);
                                }
                            }
                        }
                    }
                }
            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        });

        jPanelFoot = new JPanel();
        this.add(jPanelFoot, BorderLayout.SOUTH);
        jPanelFoot.setLayout(new VerticalFlowLayout());
        // 选择文件部分
        jPanelSelectFile = new JPanel();
        jPanelSelectFile.setLayout(new FlowLayout(FlowLayout.CENTER));
        jPanelFoot.add(jPanelSelectFile);

        // 读取配置文件
        for (SettingTemplateItem item : templateSelectedState.keySet()) {
            JCheckBox jCheckBox = new JCheckBox(item.getName());
            jCheckBox.addActionListener((l) -> {
                JCheckBox source = ((JCheckBox) l.getSource());
                String name = source.getText();
                boolean isSelected = source.isSelected();
                for (SettingTemplateItem settingTemplateItem : templateSelectedState.keySet()) {
                    if (settingTemplateItem.getName().equals(name)) {
                        templateSelectedState.put(settingTemplateItem, isSelected);
                    }
                }
            });
            jPanelSelectFile.add(jCheckBox);
        }

        // 操作部分
        jPanelOperate = new JPanel();
        jPanelFoot.add(jPanelOperate);
        jButtonSave = new JButton("Save");
        jButtonSave.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSaveClick();
            }
        });
        jButtonGenerate = new JButton("Generate");
        jButtonGenerate.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGenerateClick();
            }
        });
        jButtonCancel = new JButton("Cancel");
        jButtonCancel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancelClick();
            }
        });
        jPanelOperate.setLayout(new FlowLayout());
        jPanelOperate.add(jButtonSave);
        jPanelOperate.add(jButtonGenerate);
        jPanelOperate.add(jButtonCancel);

        this.setTitle("Mybatis Generator");
        this.setSize(1000, 600);
        setCenterLocation();
        this.setVisible(true);
    }

    public void addOperateListener(CodeGenerateMainFrameOperateEvent eventListener) {
        eventListenerList.add(eventListener);
    }

    public void remove(EventListener eventListener) {
        eventListenerList.remove(eventListener);
    }

    private void triggerOperateEvent(int operate, String... params) {
        for (CodeGenerateMainFrameOperateEvent codeGenerateMainFrameOperateEvent : eventListenerList) {
            codeGenerateMainFrameOperateEvent.active(operate, params);
        }
    }

    private void setCenterLocation() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2);
    }

    void onSaveClick() {
        triggerOperateEvent(Operate_Save);
    }

    void onGenerateClick() {
        if (templateSelectedState.values().stream().filter(i -> i.booleanValue()).count() == 0) {
            Messages.showErrorDialog("Firstly,Select a file needed to be generated", "Notice");
            return;
        }
        String[] templateNames = templateSelectedState.keySet().stream().filter(i -> templateSelectedState.get(i).booleanValue()).map(i -> i.getName()).toArray(String[]::new);
        triggerOperateEvent(Operate_Generate, templateNames);
    }

    void onCancelClick() {
        // 退出关闭页面
        this.setVisible(false);
        this.dispose();
    }

    class MyTableModel implements TableModel {
        GenerateMybatisConfigClass generateMybatisConfigClass;


        MyTableModel(GenerateMybatisConfigClass generateMybatisConfigClass) {
            this.generateMybatisConfigClass = generateMybatisConfigClass;
        }

        @Override
        public int getRowCount() {
            return generateMybatisConfigClass.getFields().size();
        }

        @Override
        public int getColumnCount() {
            // name ，

            return 6;
        }

        @Nls
        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "FieldName";
                case 1:
                    return "JavaType";
                case 2:
                    return "Ignore";
                case 3:
                    return "PrimaryKey";
                case 4:
                    return "JdbcType";
                case 5:
                    return "TypeHandler";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class<?> clz;
            switch (columnIndex) {
                case 0:
                case 1:
                case 4:
                case 5:
                    clz = String.class;
                    break;
                case 2:
                case 3:
                    clz = Boolean.class;
                    break;
                default:
                    clz = Object.class;
            }
            return clz;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (columnIndex >= 2) ? true : false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            GenerateMybatisConfigField generateMybatisConfigField = generateMybatisConfigClass.getFields().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return generateMybatisConfigField.getName();
                case 1:
                    return generateMybatisConfigField.getJavaType();
                case 2:
                    return generateMybatisConfigField.isIgnore();
                case 3:
                    return generateMybatisConfigField.isPrimaryKey();
                case 4:
                    return generateMybatisConfigField.getJdbcType();
                case 5:
                    return generateMybatisConfigField.getTypeHandler();
                default:
                    return "";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 2) {
                generateMybatisConfigClass.getFields().get(rowIndex).setIgnore((boolean) aValue);
            } else if (columnIndex == 3) {
                generateMybatisConfigClass.getFields().get(rowIndex).setPrimaryKey((Boolean) aValue);
            } else if (columnIndex == 4) {
                generateMybatisConfigClass.getFields().get(rowIndex).setJdbcType((String) aValue);
            } else if (columnIndex == 5) {
                generateMybatisConfigClass.getFields().get(rowIndex).setTypeHandler((String) aValue);
            }
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }
}
