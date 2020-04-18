package me.zeroeightsix.waypoints.impl.screen

import spinnery.client.TextRenderer
import spinnery.widget.WStaticText

class WCenteredText : WStaticText() {

    override fun draw() {
        if (isHidden()) {
            return
        }

        TextRenderer.pass().text(getText()).font(font).at(x, y + size.height / 2 - TextRenderer.height() / 2, z).scale(scale).maxWidth(maxWidth)
            .shadow(style.asBoolean("shadow")).shadowColor(style.asColor("shadowColor"))
            .color(style.asColor("text")).render()
    }

}