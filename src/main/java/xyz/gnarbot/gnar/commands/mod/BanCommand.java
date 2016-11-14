package xyz.gnarbot.gnar.commands.mod;

import com.google.inject.Inject;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.utils.PermissionUtil;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.members.Clearance;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "ban", clearance = Clearance.BOT_COMMANDER)
public class BanCommand extends CommandExecutor
{
    @Inject
    public Host host;

    @Override
    public void execute(Note msg, String label, String[] args)
    {
        Member author = msg.getAuthor();
        Member target = null;
        try {
            try { //Checks if they mentioned someone
                target = msg.getMentionedUsers().get(0);
            } catch (Exception e) { //No mention user, maybe checks if they supplied a username?
                target = msg.getGuild().getMembersByName(args[0], true).get(0);
            }
        } catch (Exception e) {
            msg.reply("User is not on this server");
            return;
        }

        if(target != null) {
            if (PermissionUtil.checkPermission(msg.getTextChannel(), author, Permission.BAN_MEMBERS)) {
                if (PermissionUtil.canInteract(author, target)) {
                    if(host.ban(target.getUser().getId())) {
                        msg.reply(target.getEffectiveName() + " has been bannerino'd");
                    } else {
                        msg.reply("GNAR does not have permission to ban");
                    }
                } else {
                    msg.reply("Sorry, you can not ban that user as he is above you.");
                }
            } else {
                msg.reply("You do not have permission to ban");
            }
        } else {
            msg.reply("Could not find user, maybe you made a typo?");
        }
    }
}