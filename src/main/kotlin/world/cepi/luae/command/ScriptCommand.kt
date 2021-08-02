package world.cepi.luae.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.literal
import world.cepi.luae.script.Script
import world.cepi.luae.script.scriptString

/**
 * Create and manage item scripts.
 */
object ScriptCommand : Command("script") {

    init {
        val create = "create".literal()
        val run = "run".literal()
        val list = "list".literal()
        val quick = "quick".literal()
        val quickContent = ArgumentType.StringArray("quickContent").map {
            it.joinToString(" ")
        }

        addSyntax(list) {
            val player = sender as Player

            val script = player.itemInMainHand.scriptString ?: return@addSyntax

            script.split("\n").forEach {
                player.sendMessage(Component.text(it, NamedTextColor.GRAY))
            }
        }

        addSyntax(create) {
            val player = sender as Player

            player.inventory.addItemStack(Script().asItem())
        }

        addSyntax(run) {
            val player = sender as Player

            val script = player.itemInMainHand.scriptString ?: return@addSyntax

            Script(script).runAsSender(player)
        }

        addSyntax(quick, quickContent) {
            Script(context[quickContent]).runAsSender(sender)
        }

        addSubcommand(object : Command("editor") {
            init {
                addSubcommand(BookScriptEditor)
                addSubcommand(LineScriptEditor)
            }
        })

    }

}