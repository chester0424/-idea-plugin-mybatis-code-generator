package zone.pusu.mybatisCodeGenerator.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nls;
import zone.pusu.mybatisCodeGenerator.common.MCGException;
import zone.pusu.mybatisCodeGenerator.config.*;
import zone.pusu.mybatisCodeGenerator.define.*;
import zone.pusu.mybatisCodeGenerator.tool.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CodeGenerateMainFrame extends JFrame {
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
    GenerateConfig generateConfig;
    ClassInfo classInfo;
    // 记录模板是否需要生成代码
    private Map<String, Boolean> templateSelectedState = new LinkedHashMap<>();
    private Config config = null;

    public CodeGenerateMainFrame(ClassInfo classInfo) {
        escToClose();

        // 初始化数据
        this.classInfo = classInfo;
        try {
            config = ConfigManager.getConfig();
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), "get Config Error");
        }
        this.generateConfig = initGenerateConfig(classInfo);

        // 读取配置的模板文件
        if (config.getTemplates() != null && config.getTemplates().keySet().size() > 0) {
            for (String s : config.getTemplates().keySet()) {
                templateSelectedState.put(s, false);
            }
        }

        // 界面UI设置
        jPanelHead = new JPanel();
        jPanelHead.setBorder(new EmptyBorder(20, 20, 0, 20));
        jPanelHead.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(jPanelHead, BorderLayout.NORTH);
        JLabel jLabelTableName = new JLabel("TableName:");
        textFieldTableName = new JTextField();
        textFieldTableName.setText(generateConfig.getTableName());
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
                generateConfig.setTableName(text);
            }
        });
        jPanelHead.add(jLabelTableName);
        jPanelHead.add(textFieldTableName);

        // 数据部分
        jPanelCenter = new JPanel();
        jPanelCenter.setLayout(new BorderLayout());
        jPanelCenter.setBorder(new EmptyBorder(10, 20, 10, 20));
        this.add(jPanelCenter, BorderLayout.CENTER);

        table = new JBTable();
        table.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        table.setRowHeight(32);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(1, 50));
        // 滚动
        jScrollPane = new JBScrollPane(table);
        jPanelCenter.add(jScrollPane);

        table.setModel(new TableModel() {
            @Override
            public int getRowCount() {
                return generateConfig.getFields().size();
            }

            @Override
            public int getColumnCount() {
                int baseCount = 8;
                long extendCount = config.getExtendColumns().size();
                return (int) (baseCount + extendCount);
            }

            @Nls
            @Override
            public String getColumnName(int columnIndex) {
                if (columnIndex <= 7) { // 基本列
                    switch (columnIndex) {
                        case 0:
                            return "FieldName";
                        case 1:
                            return "JavaType";
                        case 2:
                            return "Comment";
                        case 3:
                            return "ColumnName";
                        case 4:
                            return "Ignore";
                        case 5:
                            return "PrimaryKey";
                        case 6:
                            return "JdbcType";
                        case 7:
                            return "TypeHandler";
                        default:
                            return "";
                    }
                } else { // 自定义列
                    int index = columnIndex - 8;
                    return config.getExtendColumns().get(index).getName();
                }
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                Class<?> clz;
                if (columnIndex <= 7) {
                    switch (columnIndex) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 6:
                        case 7:
                            clz = String.class;
                            break;
                        case 4:
                        case 5:
                            clz = Boolean.class;
                            break;
                        default:
                            clz = Object.class;
                    }
                } else {
                    int index = columnIndex - 8;
                    ExtendColumn extendColumn = config.getExtendColumns().get(index);
                    if (extendColumn.getType().equals(ExtendColumnTypeEnum.BOOLEAN.name())) {
                        clz = Boolean.class;
                    } else if (extendColumn.getType().equals(ExtendColumnTypeEnum.SELECT.name()) || extendColumn.getType().equals(ExtendColumnTypeEnum.INPUT.name())) {
                        clz = String.class;
                    } else {
                        throw new MCGException("NOT SUPPORT");
                    }
                }
                return clz;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return (columnIndex >= 3) ? true : false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                GenerateConfigField generateConfigField = generateConfig.getFields().get(rowIndex);
                if (columnIndex <= 7) {
                    switch (columnIndex) {
                        case 0:
                            return generateConfigField.getName();
                        case 1:
                            return generateConfigField.getJavaType();
                        case 2:
                            return generateConfigField.getComment();
                        case 3:
                            return generateConfigField.getColumnName();
                        case 4:
                            return generateConfigField.isIgnore();
                        case 5:
                            return generateConfigField.isPrimaryKey();
                        case 6:
                            return generateConfigField.getJdbcType();
                        case 7:
                            return generateConfigField.getTypeHandler();
                        default:
                            return "";
                    }
                } else {
                    int index = columnIndex - 8;
                    String key = generateConfigField.getExtend().keySet().stream().collect(Collectors.toList()).get(index);
                    return generateConfigField.getExtend().get(key);
                }
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                GenerateConfigField generateConfigField = generateConfig.getFields().get(rowIndex);
                if (columnIndex == 3) {
                    generateConfigField.setColumnName((String) aValue);
                } else if (columnIndex == 4) {
                    generateConfigField.setIgnore((boolean) aValue);
                } else if (columnIndex == 5) {
                    generateConfigField.setPrimaryKey((Boolean) aValue);
                } else if (columnIndex == 6) {
                    generateConfigField.setJdbcType((String) aValue);
                } else if (columnIndex == 7) {
                    generateConfigField.setTypeHandler((String) aValue);
                } else {
                    int index = columnIndex - 8;
                    String key = generateConfigField.getExtend().keySet().stream().collect(Collectors.toList()).get(index);
                    generateConfigField.getExtend().put(key, aValue);
                }
            }

            @Override
            public void addTableModelListener(TableModelListener l) {

            }

            @Override
            public void removeTableModelListener(TableModelListener l) {

            }
        });

        // Jdbc Type 下拉列表
        JComboBox comboBoxJdbcType = new ComboBox();
        comboBoxJdbcType.addItem("");
        for (String allJdbcType : JdbcTypeUtil.getAllJdbcTypes()) {
            comboBoxJdbcType.addItem(allJdbcType);
        }
        TableCellEditor tableCellEditorJdbcType = new DefaultCellEditor(comboBoxJdbcType);
        table.getColumnModel().getColumn(6).setCellEditor(tableCellEditorJdbcType);
        // 扩展列下拉处理
        for (int i = 0; i < config.getExtendColumns().size(); i++) {
            ExtendColumn item = config.getExtendColumns().get(i);
            if (item.getType().equals(ExtendColumnTypeEnum.SELECT.name())) {
                if (!StringUtil.isNullOrEmpty(item.getOptions())) {
                    JComboBox jComboBox = new ComboBox();
                    for (String s : item.getOptions().split(",")) {
                        jComboBox.addItem(s);
                    }
                    table.getColumnModel().getColumn(8 + i).setCellEditor(new DefaultCellEditor(jComboBox));
                }
            }
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                int columnIndex = table.getSelectedColumn();
                if (columnIndex == 5) {
                    Boolean selected = (Boolean) table.getValueAt(rowIndex, 5);
                    if (selected) {
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (i != rowIndex) {
                                boolean val = (Boolean) table.getValueAt(i, 5);
                                if (val) {
                                    table.setValueAt(false, i, 5);
                                }
                            }
                        }
                    }
                }
            }
        });

        jPanelFoot = new JPanel();
        this.add(jPanelFoot, BorderLayout.SOUTH);
        jPanelFoot.setLayout(new VerticalFlowLayout());
        // 选择文件部分
        jPanelSelectFile = new JPanel();
        jPanelSelectFile.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanelSelectFile.setLayout(new VerticalFlowLayout());//;
        jPanelFoot.add(jPanelSelectFile);

        JPanel jPanelSelectFile_head = new JPanel();
        jPanelSelectFile_head.setLayout(new FlowLayout(FlowLayout.CENTER));
        jPanelSelectFile.add(jPanelSelectFile_head);
        JPanel jPanelSelectFile_content = new JPanel();
        jPanelSelectFile_content.setLayout(new FlowLayout(FlowLayout.CENTER));
        jPanelSelectFile.add(jPanelSelectFile_content);

        JLabel jLabel_template_text = new JLabel();
        jLabel_template_text.setText("Optional templates:  ");
        jPanelSelectFile_head.add(jLabel_template_text);

        JCheckBox jCheckBox_selectAll = new JCheckBox("Select All");
        jCheckBox_selectAll.addActionListener((l) -> {
            boolean isSelectedAll = jCheckBox_selectAll.isSelected();
            for (Component component : jPanelSelectFile_content.getComponents()) {
                ((JCheckBox) component).setSelected(isSelectedAll);
            }
            for (String s : config.getTemplates().keySet()) {
                templateSelectedState.put(s, isSelectedAll);
            }
        });
        jPanelSelectFile_head.add(jCheckBox_selectAll);

        // 读取配置文件
        for (String item : templateSelectedState.keySet()) {
            JCheckBox jCheckBox = new JCheckBox(item);
            jCheckBox.addActionListener((l) -> {
                JCheckBox source = ((JCheckBox) l.getSource());
                String name = source.getText();
                boolean isSelected = source.isSelected();
                for (String settingTemplateItem : templateSelectedState.keySet()) {
                    if (settingTemplateItem.equals(name)) {
                        templateSelectedState.put(settingTemplateItem, isSelected);
                    }
                }
                if (Arrays.stream(jPanelSelectFile_content.getComponents()).filter(i -> !((JCheckBox) i).isSelected()).findFirst().isPresent()) {
                    jCheckBox_selectAll.setSelected(false);
                }
            });
            jPanelSelectFile_content.add(jCheckBox);
        }

        // 操作部分
        jPanelOperate = new JPanel();
        jPanelFoot.add(jPanelOperate);
        jButtonSave = new JButton("Save Config");
        jButtonSave.setPreferredSize(new Dimension(200, 50));
        jButtonSave.setBackground(Color.MAGENTA);
        jButtonSave.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSaveClick();
            }
        });
        jButtonGenerate = new JButton("Generate Code");
        jButtonGenerate.setPreferredSize(new Dimension(200, 50));
        jButtonGenerate.setBackground(Color.ORANGE);
        jButtonGenerate.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGenerateClick();
            }
        });
        jButtonCancel = new JButton("Exit");
        jButtonCancel.setPreferredSize(new Dimension(200, 50));
        jButtonCancel.setBackground(Color.RED);
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

        String title = "Mybatis Generator" + " : " + classInfo.getPackageName() + '.' + classInfo.getName() + (StringUtil.isNullOrEmpty(classInfo.getComment()) ? "" : "[" + classInfo.getComment() + "]");
        this.setTitle(title);
        this.setSize(1200, 600);
        Common.setCenterLocation(this);
        this.setVisible(true);
    }

    private GenerateConfig initGenerateConfig(ClassInfo classInfo) {
        GenerateConfig result = new GenerateConfig();
        // 通过classInfo 构造配置信息
        result.setTableName(classInfo.getName()); // 默认把对象名设置为表名
        result.setComment(classInfo.getComment());
        result.setFields(new ArrayList<>());
        for (FieldInfo fieldInfo : classInfo.getFieldInfos()) {
            GenerateConfigField field = new GenerateConfigField();
            field.setName(fieldInfo.getName());
            field.setJavaType(fieldInfo.getType());
            field.setJdbcType(getJdbcTypeByJavaType(fieldInfo.getType()));
            field.setColumnName(StringUtil.spiteWord(fieldInfo.getName()));
            field.setComment(fieldInfo.getComment());
            if (fieldInfo.getName().equals("id")) { // 默认“ID”字段为主键
                field.setPrimaryKey(true);
            }
            if (defaultIgnore(fieldInfo.getType())) {
                field.setIgnore(true);
            }
            // 扩展配置列
            for (ExtendColumn item : config.getExtendColumns()) {
                // default value
                if (item.getType().equals(ExtendColumnTypeEnum.BOOLEAN.name())) {
                    field.getExtend().put(item.getName(), false);
                } else if (item.getType().equals(ExtendColumnTypeEnum.SELECT.name()) || item.getType().equals(ExtendColumnTypeEnum.INPUT.name())) {
                    field.getExtend().put(item.getName(), "");
                } else {
                    throw new MCGException(String.format("extend column type %s Not Support", item.getType()));
                }
            }
            result.getFields().add(field);
        }

        // 文件中读取配置信息
        String fileName = Paths.get(new File(classInfo.getFilePath()).getParent(), "dao", classInfo.getName() + "-mcfg.json").toString();//  + "/dao/" + classInfo.getName() + "-mcfg.json";
        String content = FileUtil.readFile(fileName);
        if (content != null) {
            GenerateConfig configClassFromConfigFile = JsonUtil.fromJson(content, GenerateConfig.class);
            if (!StringUtil.isNullOrEmpty(configClassFromConfigFile.getTableName())) {
                result.setTableName(configClassFromConfigFile.getTableName());
            }
            for (GenerateConfigField field : configClassFromConfigFile.getFields()) {
                Optional<GenerateConfigField> optional = result.getFields().stream().filter(i -> i.getName().equals(field.getName()) && i.getJavaType().equals(field.getJavaType())).findFirst();
                if (optional.isPresent()) {
                    if (!StringUtil.isNullOrEmpty(field.getColumnName())) {
                        optional.get().setColumnName(field.getColumnName());
                    }
                    optional.get().setIgnore(field.isIgnore());
                    optional.get().setPrimaryKey(field.isPrimaryKey());
                    if (!StringUtil.isNullOrEmpty(field.getJdbcType())) {
                        optional.get().setJdbcType(field.getJdbcType());
                    }
                    if (!StringUtil.isNullOrEmpty(field.getTypeHandler())) {
                        optional.get().setTypeHandler(field.getTypeHandler());
                    }
                    if (optional.get().getExtend().size() > 0) {
                        for (String key : optional.get().getExtend().keySet()) {
                            if (field.getExtend().containsKey(key)) {
                                optional.get().getExtend().put(key, field.getExtend().get(key));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private String getJdbcTypeByJavaType(String javaType) {
        Optional<TypeMapping> optional = config.getTypeMappings().stream().filter(i -> i.getJavaType().equals(javaType)).findFirst();
        if (optional.isPresent()) {
            return optional.get().getJdbcType();
        }
        return "";
    }

    private boolean defaultIgnore(String javaType) {
        Optional<TypeMapping> optional = config.getTypeMappings().stream().filter(i -> i.getJavaType().equals(javaType)).findFirst();
        return !optional.isPresent();
    }

    void onSaveClick() {
        String fileName = classInfo.getName() + "-mcfg.json";
        String settingConfigFileSavePath = config.getConfigFileSavePath();
        if (!StringUtil.isNullOrEmpty(settingConfigFileSavePath) && new File(settingConfigFileSavePath).isAbsolute()) {
            fileName = Paths.get(settingConfigFileSavePath, fileName).toString();
        } else {
            fileName = Paths.get(new File(classInfo.getFilePath()).getParent(), settingConfigFileSavePath, fileName).toString();
        }
        String content = JsonUtil.toJsonPretty(generateConfig);
        FileUtil.writeFile(fileName, content);

        buttonExecuteEffect(this.jButtonSave, "Saved");
    }

    void onGenerateClick() {
        try {
            if (templateSelectedState.values().stream().filter(i -> i.booleanValue()).count() == 0) {
                Messages.showErrorDialog("At least one template needs to be selected", "ERROR");
                return;
            }
            String[] templateNames = templateSelectedState.keySet().stream().filter(i -> templateSelectedState.get(i).booleanValue()).toArray(String[]::new);
            for (String templateName : templateNames) {
                for (String item : config.getTemplates().keySet()) {
                    if (item.equals(templateName)) {
                        // 默认路径和名称
                        String templateFileName = templateFileTrimEnd(templateName);
                        String fileDir = Paths.get(new File(classInfo.getFilePath()).getParent()).toString();
                        String fileName = classInfo.getName() + templateFileName;
                        TemplateDataContext templateDataContext = new TemplateDataContext(classInfo, generateConfig, fileDir, fileName);
                        String content = FreeMarkerUtil.process(templateFileName, config.getTemplates().get(item), templateDataContext);
                        String filePath = Paths.get(templateDataContext.getTargetFileDir(), templateDataContext.getTargetFileName()).toString();
                        FileUtil.writeFile(filePath, content);

                    }
                }
            }
            buttonExecuteEffect(this.jButtonGenerate, "Generated");
        } catch (Exception ex) {
            Messages.showErrorDialog(ex.getMessage(), "ERROR");
        }
    }

    private String templateFileTrimEnd(String fileName) {
        String templateFileExtend = ".ftl";
        if (fileName.endsWith(templateFileExtend)) {
            return StringUtil.trimEnd(fileName, templateFileExtend);
        }
        return fileName;
    }

    void onCancelClick() {
        // 退出关闭页面
        this.setVisible(false);
        this.dispose();
    }

    void escToClose() {
        KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        ActionListener actionListener = (ActionEvent e) -> onCancelClick();
        keyboardFocusManager.addKeyEventDispatcher((KeyEvent event) -> {
            if ((event.getID() == KeyEvent.KEY_PRESSED) && (event.getKeyCode() == KeyEvent.VK_ESCAPE)) {
                actionListener.actionPerformed(null);
            }
            return false;
        });
    }

    void buttonExecuteEffect(JButton jButton, String message) {
        String originalText = jButton.getText();
        ThreadUtil.execute(new Runnable[]{() -> {
            SwingUtilities.invokeLater(() -> jButton.setText(message));
        }, () -> {
            SwingUtilities.invokeLater(() -> jButton.setText(originalText));
        }}, 1);
    }
}
