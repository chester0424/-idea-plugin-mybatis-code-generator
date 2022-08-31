package zone.pusu.mybatisCodeGenerator.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nls;
import zone.pusu.mybatisCodeGenerator.define.GenerateMybatisConfigClass;
import zone.pusu.mybatisCodeGenerator.define.GenerateMybatisConfigField;

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

public class MyMainFrame extends JFrame {
    // 操作类型申请
    public static int Operate_Save = 1;
    public static int Operate_Generate = 2;

    public static int Generate_File_Type_Dao = 1;
    public static int Generate_File_Type_Mapper = 2;
    public static int Generate_File_Type_QueryParam = 3;
    // 数据申请
    GenerateMybatisConfigClass generateMybatisConfigClass;
    private boolean isGenerateDaoFile = false;
    private boolean isGenerateMapperFile = false;
    private boolean isGenerateQueryParamFile = false;
    // 事件申请
    private ArrayList<MyMainFrameOperateEvent> eventListenerList = new ArrayList<MyMainFrameOperateEvent>();

    public MyMainFrame(GenerateMybatisConfigClass generateMybatisConfigClass) {
        this.generateMybatisConfigClass = generateMybatisConfigClass;
        JPanel jPanelHead = new JPanel();
        jPanelHead.setBorder(new EmptyBorder(20, 20, 0, 20));
        jPanelHead.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(jPanelHead, BorderLayout.NORTH);
        JLabel jLabelTableName = new JLabel("TableName:");
        JTextField textFieldTableName = new JTextField();
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
            private void triggerChanged(){
                String text = textFieldTableName.getText();
                onTableNameChanged(text);
            }
        });
        jPanelHead.add(jLabelTableName);
        jPanelHead.add(textFieldTableName);

        // 数据部分
        JPanel jPanelCenter = new JPanel();
        jPanelCenter.setLayout(new BorderLayout());
        this.add(jPanelCenter, BorderLayout.CENTER);

        JBTable table = new JBTable();
        table.setBorder(new LineBorder(Color.GRAY, 1));
        table.setRowHeight(32);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(1, 50));
        // 滚动
        JScrollPane jScrollPane = new JBScrollPane(table);
        jScrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        jPanelCenter.add(jScrollPane);

        table.setModel(new MyTableModel(generateMybatisConfigClass));

        JComboBox comboBoxJdbcType = new ComboBox();
        comboBoxJdbcType.addItem("FLOAT");
        comboBoxJdbcType.addItem("INTEGER");
        comboBoxJdbcType.addItem("NUMERIC");
        comboBoxJdbcType.addItem("DATE");
        comboBoxJdbcType.addItem("LONGVARBINARY");
        TableCellEditor tableCellEditorJdbcType = new DefaultCellEditor(comboBoxJdbcType);
        table.getColumnModel().getColumn(2).setCellEditor(tableCellEditorJdbcType);


        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                int columnIndex = table.getSelectedColumn();
                if (columnIndex >= 3 && columnIndex <= 5) {
                    Boolean result = !(Boolean) table.getValueAt(rowIndex, columnIndex);
                    table.setValueAt(result, rowIndex, columnIndex);
                    if (columnIndex == 3) {
                        if (result) {
                            // 则其他为false
                            for (int i = 0; i < table.getRowCount(); i++) {
                                if (rowIndex != i) {
                                    boolean val = (Boolean) table.getValueAt(i, columnIndex);
                                    if (val) {
                                        table.setValueAt(false, i, columnIndex);
                                    }
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

        JPanel jPanelFoot = new JPanel();
        this.add(jPanelFoot, BorderLayout.SOUTH);
        jPanelFoot.setLayout(new VerticalFlowLayout());
        // 选择文件部分
        JPanel jPanelSelectFile = new JPanel();
        jPanelFoot.add(jPanelSelectFile);
        JCheckBox jCheckBoxDao = new JCheckBox("Dao");
        jCheckBoxDao.setSelected(isGenerateDaoFile);
        jCheckBoxDao.addActionListener((l) -> {
            isGenerateDaoFile = jCheckBoxDao.isSelected();
        });
        JCheckBox jCheckBoxMapper = new JCheckBox("Mapper");
        jCheckBoxMapper.setSelected(isGenerateMapperFile);
        jCheckBoxMapper.addActionListener(i -> {
            isGenerateMapperFile = jCheckBoxMapper.isSelected();
        });
        JCheckBox jCheckBoxQueryParam = new JCheckBox("QueryParam");
        jCheckBoxQueryParam.setSelected(isGenerateQueryParamFile);
        jCheckBoxQueryParam.addActionListener(i -> {
            isGenerateQueryParamFile = jCheckBoxQueryParam.isSelected();
        });
        jPanelSelectFile.setLayout(new FlowLayout(FlowLayout.CENTER));
        jPanelSelectFile.add(jCheckBoxDao);
        jPanelSelectFile.add(jCheckBoxMapper);
        jPanelSelectFile.add(jCheckBoxQueryParam);
        // 操作部分
        JPanel jPanelOperate = new JPanel();
        jPanelFoot.add(jPanelOperate);
        JButton jButtonSave = new JButton("Save");
        jButtonSave.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSaveClick();
            }
        });
        JButton jButtonGenerate = new JButton("Generate");
        jButtonGenerate.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGenerateClick();
            }
        });
        JButton jButtonCancel = new JButton("Cancel");
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

    public void addOperateListener(MyMainFrameOperateEvent eventListener) {
        eventListenerList.add(eventListener);
    }

    public void remove(EventListener eventListener) {
        eventListenerList.remove(eventListener);
    }

    private void triggerOperateEvent(int operate, String... params) {
        for (MyMainFrameOperateEvent myMainFrameOperateEvent : eventListenerList) {
            myMainFrameOperateEvent.active(operate, params);
        }
    }

    private void setCenterLocation() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2);
    }

    void onTableNameChanged(String tableName) {
        generateMybatisConfigClass.setTableName(tableName);
    }

    void onSaveClick() {
        triggerOperateEvent(Operate_Save);
    }

    void onGenerateClick() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        if (isGenerateDaoFile) {
            arrayList.add(Generate_File_Type_Dao);
        }
        if (isGenerateMapperFile) {
            arrayList.add(Generate_File_Type_Mapper);
        }
        if (isGenerateQueryParamFile) {
            arrayList.add(Generate_File_Type_QueryParam);
        }
        triggerOperateEvent(Operate_Generate, arrayList.toArray(String[]::new));
    }

    void onCancelClick() {
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE  );
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
                    return "JdbcType";
                case 3:
                    return "PrimaryKey";
                case 4:
                    return "QueryCondition";
                case 5:
                    return "OrderByCondition";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex <= 2) {
                return String.class;
            } else if (columnIndex >= 3 && columnIndex <= 5) {
                return boolean.class;
            } else {
                return Object.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (columnIndex == 2) ? true : false;
//            return false;
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
                    return generateMybatisConfigField.getJdbcType();
                case 3:
                    return generateMybatisConfigField.isPrimaryKey();
                case 4:
                    return generateMybatisConfigField.isQuery();
                case 5:
                    return generateMybatisConfigField.isOrderBy();
                default:
                    return "";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 2) {
                generateMybatisConfigClass.getFields().get(rowIndex).setJdbcType((String) aValue);
            } else if (columnIndex == 3) {
                generateMybatisConfigClass.getFields().get(rowIndex).setPrimaryKey((Boolean) aValue);
            } else if (columnIndex == 4) {
                generateMybatisConfigClass.getFields().get(rowIndex).setQuery((Boolean) aValue);
            } else if (columnIndex == 5) {
                generateMybatisConfigClass.getFields().get(rowIndex).setOrderBy((Boolean) aValue);
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
