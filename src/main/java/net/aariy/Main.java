package net.aariy;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;

public class Main extends ListenerAdapter
{
    public static void main(String[] args)
    {
        JDA jda = JDABuilder.createDefault(args[0]).enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        jda.updateCommands().addCommands(
                Commands.slash("起動1", "ランダムに文章を表示します。")
        ).queue();
        jda.addEventListener(new Main());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equals("起動1"))
        {
            e.deferReply().queue();
            SecureRandom sc = new SecureRandom();
            String[] list = {"文章1"};
            try
            {
                URL url = new URL("https://cdn.discordapp.com/attachments/1064793463306072175/1064793483132547102/IMG_1536.png");
                File tmp = File.createTempFile("imaage", null);
                URLConnection conn = url.openConnection();
                InputStream in = conn.getInputStream();
                FileOutputStream out = new FileOutputStream(tmp, false);
                int b;
                while((b = in.read()) != -1){
                    out.write(b);
                }
                out.close();
                in.close();
                e.getHook().sendMessage(list[sc.nextInt(1)]).addFiles(FileUpload.fromData(tmp, "imaage.png")).queue();
            }
            catch(IOException ex)
            {
                throw new RuntimeException(ex);
            }

        }
    }
}