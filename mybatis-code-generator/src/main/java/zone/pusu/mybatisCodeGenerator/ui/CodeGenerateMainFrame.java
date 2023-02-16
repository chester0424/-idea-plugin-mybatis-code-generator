package zone.pusu.mybatisCodeGenerator.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nls;
import zone.pusu.mybatisCodeGenerator.common.MCGException;
import zone.pusu.mybatisCodeGenerator.define.*;
import zone.pusu.mybatisCodeGenerator.setting.*;
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
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
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
    private Map<SettingTemplateItem, Boolean> templateSelectedState = new LinkedHashMap<>();

    public CodeGenerateMainFrame(ClassInfo classInfo) {
        // 初始化数据
        this.classInfo = classInfo;
        this.generateConfig = initGenerateConfig(classInfo);
        // 读取配置的模板文件
        for (SettingTemplateItem item : SettingTemplateStoreService.getInstance().getState().getItems()) {
            templateSelectedState.put(item, false);
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
        jPanelCenter.setBorder(new EmptyBorder(10, 20, 20, 10));
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
                // name ，
                int baseCount = 7;
                long extendCount = SettingExtendCfgColStoreService.getInstance().getState().getItems().stream().count();
                return (int) (baseCount + extendCount);
            }

            @Nls
            @Override
            public String getColumnName(int columnIndex) {
                if (columnIndex <= 6) { // 基本列
                    switch (columnIndex) {
                        case 0:
                            return "FieldName";
                        case 1:
                            return "JavaType";
                        case 2:
                            return "ColumnName";
                        case 3:
                            return "Ignore";
                        case 4:
                            return "PrimaryKey";
                        case 5:
                            return "JdbcType";
                        case 6:
                            return "TypeHandler";
                        default:
                            return "";
                    }
                } else { // 自定义列
                    int index = columnIndex - 7;
                    return SettingExtendCfgColStoreService.getInstance().getState().getItems().get(index).getName();
                }
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                Class<?> clz;
                if (columnIndex <= 6) {
                    switch (columnIndex) {
                        case 0:
                        case 1:
                        case 2:
                        case 5:
                        case 6:
                            clz = String.class;
                            break;
                        case 3:
                        case 4:
                            clz = Boolean.class;
                            break;
                        default:
                            clz = Object.class;
                    }
                } else {
                    int index = columnIndex - 7;
                    SettingExtendCfgColItem settingExtendCfgColItem = SettingExtendCfgColStoreService.getInstance().getState().getItems().get(index);
                    if (settingExtendCfgColItem.getType().equals(SettingExtendCfgColTypeEnum.BOOLEAN.name())) {
                        clz = Boolean.class;
                    } else if (settingExtendCfgColItem.getType().equals(SettingExtendCfgColTypeEnum.SELECT.name()) ||
                            settingExtendCfgColItem.getType().equals(SettingExtendCfgColTypeEnum.INPUT.name())) {
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
                if (columnIndex <= 6) {
                    switch (columnIndex) {
                        case 0:
                            return generateConfigField.getName();
                        case 1:
                            return generateConfigField.getJavaType();
                        case 2:
                            return generateConfigField.getColumnName();
                        case 3:
                            return generateConfigField.isIgnore();
                        case 4:
                            return generateConfigField.isPrimaryKey();
                        case 5:
                            return generateConfigField.getJdbcType();
                        case 6:
                            return generateConfigField.getTypeHandler();
                        default:
                            return "";
                    }
                } else {
                    int index = columnIndex - 7;
                    String key = generateConfigField.getExtend().keySet().stream().collect(Collectors.toList()).get(index);
                    return generateConfigField.getExtend().get(key);
                }
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                GenerateConfigField generateConfigField = generateConfig.getFields().get(rowIndex);
                if (columnIndex == 3) {
                    generateConfigField.setIgnore((boolean) aValue);
                } else if (columnIndex == 4) {
                    generateConfigField.setPrimaryKey((Boolean) aValue);
                } else if (columnIndex == 5) {
                    generateConfigField.setJdbcType((String) aValue);
                } else if (columnIndex == 6) {
                    generateConfigField.setTypeHandler((String) aValue);
                } else {
                    int index = columnIndex - 7;
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
        table.getColumnModel().getColumn(5).setCellEditor(tableCellEditorJdbcType);
        // 扩展列下拉处理
        for (int i = 0; i < SettingExtendCfgColStoreService.getInstance().getState().getItems().size(); i++) {
            SettingExtendCfgColItem item = SettingExtendCfgColStoreService.getInstance().getState().getItems().get(i);
            if (item.getType().equals(SettingExtendCfgColTypeEnum.SELECT.name())) {
                if (!StringUtil.isNullOrEmpty(item.getOptions())) {
                    JComboBox jComboBox = new ComboBox();
                    for (String s : item.getOptions().split(",")) {
                        jComboBox.addItem(s);
                    }
                    table.getColumnModel().getColumn(7 + i).setCellEditor(new DefaultCellEditor(jComboBox));
                }
            }
        }


        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                int columnIndex = table.getSelectedColumn();
                if (columnIndex == 4) {
                    Boolean selected = (Boolean) table.getValueAt(rowIndex, 4);
                    if (selected) {
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (i != rowIndex) {
                                boolean val = (Boolean) table.getValueAt(i, 4);
                                if (val) {
                                    table.setValueAt(false, i, 4);
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
        jPanelSelectFile.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanelSelectFile.setLayout(new FlowLayout(FlowLayout.CENTER));
        jPanelFoot.add(jPanelSelectFile);

        JLabel jLabel_template_text = new JLabel();
        jLabel_template_text.setText("可选模板:  ");
        jPanelSelectFile.add(jLabel_template_text);

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

        this.setTitle("Mybatis Generator" + " : " + classInfo.getPackageName() + '.' + classInfo.getName());
        this.setSize(1200, 600);
        Common.setCenterLocation(this);
        this.setVisible(true);
    }

    private GenerateConfig initGenerateConfig(ClassInfo classInfo) {
        GenerateConfig result = new GenerateConfig();
        // 通过classInfo 构造配置信息
        result.setTableName(classInfo.getName()); // 默认把对象名设置为表名
        result.setFields(new ArrayList<>());
        for (FieldInfo fieldInfo : classInfo.getFieldInfos()) {
            GenerateConfigField field = new GenerateConfigField();
            field.setName(fieldInfo.getName());
            field.setJavaType(fieldInfo.getType());
            field.setJdbcType(getJdbcTypeByJavaType(fieldInfo.getType()));
            field.setColumnName(StringUtil.spiteWord(fieldInfo.getName()));

            // 扩展配置列
            for (SettingExtendCfgColItem item : SettingExtendCfgColStoreService.getInstance().getState().getItems()) {
                // default value
                if (item.getType().equals(SettingExtendCfgColTypeEnum.BOOLEAN.name())) {
                    field.getExtend().put(item.getName(), false);
                } else if (item.getType().equals(SettingExtendCfgColTypeEnum.SELECT.name()) ||
                        item.getType().equals(SettingExtendCfgColTypeEnum.INPUT.name())) {
                    field.getExtend().put(item.getName(), "");
                } else {
                    throw new MCGException("Not Support");
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
                    if (field.isIgnore()) {
                        optional.get().setIgnore(field.isIgnore());
                    }
                    if (!StringUtil.isNullOrEmpty(field.getColumnName())) {
                        optional.get().setColumnName(field.getColumnName());
                    }
                    if (!StringUtil.isNullOrEmpty(field.getColumnName())) {
                        optional.get().setColumnName(field.getColumnName());
                    }
                    if (!StringUtil.isNullOrEmpty(field.getJdbcType())) {
                        optional.get().setJdbcType(field.getJdbcType());
                    }
                    if (field.isPrimaryKey()) {
                        optional.get().setPrimaryKey(field.isPrimaryKey());
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
        Optional<SettingTypeMappingItem> optional = SettingTypeMappingStoreService.getInstance().getState().getItems().stream().filter(i -> i.getJavaType().equals(javaType)).findFirst();
        if (optional.isPresent()) {
            return optional.get().getJdbcType();
        }
        return "";
    }

    void onSaveClick() {
        String fileName = classInfo.getName() + "-mcfg.json";
        String settingConfigFileSavePath = SettingMainStoreService.getInstance().getState().getConfigFileSavePath();
        if (!StringUtil.isNullOrEmpty(settingConfigFileSavePath) && new File(settingConfigFileSavePath).isAbsolute()) {
            fileName = Paths.get(settingConfigFileSavePath, fileName).toString();
        } else {
            fileName = Paths.get(new File(classInfo.getFilePath()).getParent(), settingConfigFileSavePath, fileName).toString();
        }
        String content = JsonUtil.toJsonPretty(generateConfig);
        FileUtil.writeFile(fileName, content);
        Messages.showInfoMessage("Saved Successfully", "Notice");
    }

    void onGenerateClick() {
        try {
            if (templateSelectedState.values().stream().filter(i -> i.booleanValue()).count() == 0) {
                Messages.showErrorDialog("At least one template needs to be selected", "ERROR");
                return;
            }
            String[] templateNames = templateSelectedState.keySet().stream().filter(i -> templateSelectedState.get(i).booleanValue()).map(i -> i.getName()).toArray(String[]::new);
            for (String templateName : templateNames) {
                for (SettingTemplateItem item : SettingTemplateStoreService.getInstance().getState().getItems()) {
                    if (item.getName().equals(templateName)) {
                        // 默认路径和名称
                        String fileDir = Paths.get(new File(classInfo.getFilePath()).getParent()).toString();
                        String fileName = classInfo.getName() + templateName;
                        TemplateDataContext templateDataContext = new TemplateDataContext(classInfo, generateConfig, fileDir, fileName);
                        String content = FreeMarkerUtil.process(item.getContent(), templateDataContext);
                        String filePath = Paths.get(templateDataContext.getTargetFileDir(), templateDataContext.getTargetFileName()).toString();
                        FileUtil.writeFile(filePath, content);

                    }
                }
            }
            Messages.showInfoMessage("Generated Successfully", "Notice");
        } catch (Exception ex) {
            Messages.showErrorDialog(ex.getMessage(), "ERROR");
        }
    }

    void onCancelClick() {
        // 退出关闭页面
        this.setVisible(false);
        this.dispose();
    }
}
