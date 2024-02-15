package sk.lukaspatrnciak.blogapplication.config;

import sk.lukaspatrnciak.blogapplication.models.Account;
import sk.lukaspatrnciak.blogapplication.models.Authority;
import sk.lukaspatrnciak.blogapplication.models.Post;
import sk.lukaspatrnciak.blogapplication.repositories.AuthorityRepository;
import sk.lukaspatrnciak.blogapplication.services.AccountService;
import sk.lukaspatrnciak.blogapplication.services.FileService;
import sk.lukaspatrnciak.blogapplication.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private FileService fileService;

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) {

        fileService.init();

        List<Post> posts = postService.getAll();

        if (posts.isEmpty()) {

            Authority user = new Authority();
            user.setName("ROLE_USER");
            authorityRepository.save(user);

            Authority admin = new Authority();
            admin.setName("ROLE_ADMIN");
            authorityRepository.save(admin);

            Account userAccount = Account
                    .builder()
                    .firstName("User")
                    .lastName("Surname")
                    .email("user@domain.com")
                    .password("password")
                    .build();

            Set<Authority> userAuthorities = new HashSet<>();
            authorityRepository.findById("ROLE_USER").ifPresent(userAuthorities::add);
            userAccount.setAuthorities(userAuthorities);

            Account adminAccount = Account
                    .builder()
                    .firstName("Admin")
                    .lastName("Surname")
                    .email("admin@domain.com")
                    .password("password")
                    .build();

            Set<Authority> adminAuthorities = new HashSet<>();
            authorityRepository.findById("ROLE_ADMIN").ifPresent(adminAuthorities::add);
            adminAccount.setAuthorities(adminAuthorities);

            accountService.save(userAccount);
            accountService.save(adminAccount);

            Post userPost = Post
                    .builder()
                    .title("Čo je to World of Tanks?")
                    .body("World of tanks je hra žánru MMORPG, vyvíjaná firmou Wargaming.net, v ktorej hráč hrá z pohľadu vodiča a strelca tanku. K dispozícií sú vozidlá a prototypy tankov prevažne z obdobia druhej svetovej vojny a začiatku studenej vojny. V hre máte možnosť vybrať si z pomedzi viac než 600 rôznych hrateľných tankov a 11 národov: ZSSR, Nemecko, USA, Británia, Francúzsko, Čína , Japonsko, Česko-Slovensko, Švédsko, Taliansko a Poľsko. V hre nájdete mnoho historických tankov ako T-34, T-54, Pz.Kpfw VI Tiger, KV-1, M4 Sherman, ale aj tanky, ktoré zostali len na papieri ako OBJ-705A, IS-7 či E-100.")
                    .account(userAccount)
                    .imageFilePath("PostImage.png")
                    .build();

            Post adminPost = Post
                    .builder()
                    .title("Rozdelenie tankov vo World of Tanks")
                    .body("V hre sú tanky rozdelené do takzvaných tierov (úrovní), tanky sú radené do 10 úrovní podľa ich sily, je to tak preto aby spolu hrali rovnako silné tanky, čím vyššia úroveň, tým je tank drahší a silnejší a má väčšiu výdrž. Tiež sa tanky delia na kategórie: Ťažké (heavy), stredné (medium), ľahké (light), stíhače tankov (tank destroyers), delostrelectvá (Artillery/SPG).")
                    .account(adminAccount)
                    .imageFilePath("PostImage.png")
                    .build();

            postService.save(userPost);
            postService.save(adminPost);
        }
    }

}
