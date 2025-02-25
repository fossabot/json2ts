import com.intellij.ui.components.JBScrollPane
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.Theme
import parser.ParseType
import java.awt.Dimension
import java.awt.Insets
import java.io.IOException
import javax.swing.*

class Json2TsForm {
    var rootView: JPanel? = null
    var editor: RSyntaxTextArea? = null
    var generateButton: JButton?=null
    var rootObjectName: JTextField? = null
    var fileNameLabel: JLabel? = null
    var typeRadio: JRadioButton? = null
    var interfaceRadio: JRadioButton? = null
    var jsDocRadio: JRadioButton? = null
    var buttonGroup:ButtonGroup? = null
    private var listener: OnGenerateClicked? = null

    fun setOnGenerateListener(listener: OnGenerateClicked) {
        this.listener = listener
        interfaceRadio!!.isSelected = true
        val radioList = listOf<JRadioButton?>(interfaceRadio, jsDocRadio, typeRadio)
        radioList.forEach{
            it!!.addActionListener{
                radioList
                        .filter { btn -> btn!!.text != it!!.actionCommand }
                        .forEach{btn -> btn!!.isSelected = false}
            }
        }
        generateButton!!.addActionListener {
            val selectedRadio = radioList.find { it!!.isSelected}
            val parseType = when {
                selectedRadio!!.text == "jsDoc" -> {
                    ParseType.JsDoc
                }
                selectedRadio.text == "type" -> {
                     ParseType.TypeStruct
                }
                else -> ParseType.InterfaceStruct
            }
            val rootName = if (rootObjectName!!.text != "") {rootObjectName!!.text}  else "RootObject"
            this.listener?.onClicked(
                    rootName,
                if (editor != null) editor!!.text else "",
                    parseType
            )
        }
    }

    private fun createUIComponents() {


        editor = RSyntaxTextArea()
        editor!!.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
        editor!!.isCodeFoldingEnabled = true
        try {
            val theme = Theme.load(
                javaClass.getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/monokai.xml"
                )
            )
            theme.apply(editor!!)
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }

    }

    init {
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        `$$$setupUI$$$`()
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     * @noinspection ALL
     */
    private fun `$$$setupUI$$$`() {
        createUIComponents()
        rootView = JPanel()
        rootView!!.layout = GridLayoutManager(2, 4, Insets(0, 0, 0, 0), -1, -1)
        rootView!!.preferredSize = Dimension(500, 500)
        val scrollPane1 = JBScrollPane()
        rootView!!.add(
            scrollPane1,
            GridConstraints(
                0,
                0,
                1,
                4,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_WANT_GROW,
                null,
                null,
                null,
                0,
                false
            )
        )
        scrollPane1.setViewportView(editor)
        generateButton = JButton()
        generateButton!!.text = "Generate"
        rootView!!.add(
            generateButton!!,
            GridConstraints(
                1,
                3,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false
            )
        )

        rootObjectName = JTextField()
        rootView!!.add(
            rootObjectName!!,
            GridConstraints(
                1,
                2,
                1,
                1,
                GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null,
                Dimension(150, -1),
                null,
                0,
                false
            )
        )
        fileNameLabel = JLabel()
        fileNameLabel!!.text = "Root name:"
        rootView!!.add(
            fileNameLabel!!,
            GridConstraints(
                1,
                1,
                1,
                1,
                GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false
            )
        )

        buttonGroup = ButtonGroup()
        buttonGroup!!.add(typeRadio)
        buttonGroup!!.add(interfaceRadio)
        buttonGroup!!.add(jsDocRadio)
    }

    /**
     * @noinspection ALL
     */
    fun `$$$getRootComponent$$$`(): JComponent? {
        return rootView
    }

    interface OnGenerateClicked {
        fun onClicked(rootName: String, json: String, parseType: ParseType)
    }

}
