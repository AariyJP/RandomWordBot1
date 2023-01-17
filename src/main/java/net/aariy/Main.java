package net.aariy;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.security.SecureRandom;
import java.util.Base64;

public class Main extends ListenerAdapter
{
    public static void main(String[] args)
    {
        JDA jda = JDABuilder.createDefault(args[0]).enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        jda.updateCommands().addCommands(
                Commands.slash("わびスロット", "ランダムにスロットを表示します。"),
                Commands.slash("reset", "スロットの回数の記録を削除します。").setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        ).queue();
        jda.addEventListener(new Main());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equals("起動1"))
        {
            SecureRandom sc = new SecureRandom();
            String[] list = {"文章1"};
            e.reply(list[sc.nextInt(1)]).addFiles(FileUpload.fromData(Base64.getDecoder().decode(Base.image64[0]), "image1.jpg")).queue();

        }
    }
}