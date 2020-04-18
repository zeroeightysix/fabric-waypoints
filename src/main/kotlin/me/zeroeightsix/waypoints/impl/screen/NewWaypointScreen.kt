package me.zeroeightsix.waypoints.impl.screen

import me.zeroeightsix.waypoints.api.WaypointRenderer
import me.zeroeightsix.waypoints.api.Waypoints
import me.zeroeightsix.waypoints.impl.WaypointImpl
import net.minecraft.client.MinecraftClient
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.Vec3d
import org.lwjgl.glfw.GLFW
import spinnery.client.BaseScreen
import spinnery.client.TextRenderer
import spinnery.widget.*
import spinnery.widget.api.Position
import spinnery.widget.api.Size
import spinnery.widget.api.WLayoutElement
import spinnery.widget.api.WPositioned

class NewWaypointScreen : BaseScreen() {

    private fun Position.add(x: Int, y: Int): Position = add(x, y, 0)
    private fun WLayoutElement.lower(yOffset: Int): Position = Position.ofBottomLeft(this).add(0, yOffset)
    private fun WAbstractTextEditor.active(active: Boolean = true) = setActive<WAbstractTextEditor>(active)
    private fun WAbstractWidget.nextTo(xOffset: Int): Position = Position.ofTopRight(this).add(xOffset, 0, 0)
    private fun WAbstractTextEditor.tabNext(next: WAbstractTextEditor, lamda: (WAbstractTextEditor, Int, Int, Int) -> Unit = {_,_,_,_ -> }) = setOnKeyPressed<WAbstractTextEditor> { widget, keyPressed, character, keyModifier ->
        if (keyPressed == GLFW.GLFW_KEY_TAB) {
            future = {
                active(false)
                next.active()
            }
        } else {
            lamda(widget, keyPressed, character, keyModifier)
        }
    }

    private var future: () -> Unit = {};

    init {
        val panel = `interface`.createChild(
            { WPanel() },
            Position.ORIGIN,
            Size.of(200, 108)
        ).setOnAlign<WPanel> { it.center() }
        panel.center()

        val label = panel.createChild(
            { WStaticText() },
            Position.of(panel).add(6, 8)
        ).setText<WStaticText>(TranslatableText("gui.waypoints.new_waypoint.label_new"))

        val nameField = panel.createChild(
            { WTextField() },
            label.lower(8),
            Size.of(200 - 6 * 2, 16)
        ).active()

        val createButtonText = TranslatableText("gui.waypoints.new_waypoint.create_button")
        val createButtonSize = Size.of(TextRenderer.width(createButtonText) + 6, 16)
        panel.createChild(
            { WButton() },
            Position.ofTopRight(panel).add(-(6 + createButtonSize.width), 6),
            createButtonSize
        ).setLabel<WButton>(createButtonText)

        val xLabel = panel.createChild(
            { WCenteredText() },
            nameField.lower(10),
            Size.of(0, 16)
        ).setText<WStaticText>("X")

        val yLabel = panel.createChild(
            { WCenteredText() },
            xLabel.lower(8),
            Size.of(0, 16)
        ).setText<WStaticText>("Y")

        val zLabel = panel.createChild(
            { WCenteredText() },
            yLabel.lower(8),
            Size.of(0, 16)
        ).setText<WStaticText>("Z")

        val xField = panel.createChild(
            { WNumberField() },
            xLabel.nextTo(12),
            Size.of(60, 16)
        ).setText<WNumberField>(MinecraftClient.getInstance().player!!.x.toInt().toString())
        val yField = panel.createChild(
            { WNumberField() },
            yLabel.nextTo(12),
            Size.of(60, 16)
        ).setText<WNumberField>(MinecraftClient.getInstance().player!!.eyeY.toInt().toString())
        val zField = panel.createChild(
            { WNumberField() },
            zLabel.nextTo(12),
            Size.of(60, 16)
        ).setText<WNumberField>(MinecraftClient.getInstance().player!!.z.toInt().toString())

        // Tab order
        nameField.tabNext(xField) { widget, keyPressed, _, _ ->
            if (keyPressed == GLFW.GLFW_KEY_ENTER) {
                val name = widget.text
                val x = xField.text.toDouble() + .5
                val y = yField.text.toDouble()
                val z = zField.text.toDouble() + .5
                val pos = Vec3d(x, y, z)
                Waypoints.accessor.addWaypoint(
                    WaypointImpl(pos, name, WaypointRenderer.default)
                )

                onClose()
            }
        }
        xField.tabNext(yField)
        yField.tabNext(zField)
    }

    override fun keyPressed(keyCode: Int, character: Int, keyModifier: Int): Boolean {
        return super.keyPressed(keyCode, character, keyModifier).also {
            future()
            future = {}
        }
    }

}
