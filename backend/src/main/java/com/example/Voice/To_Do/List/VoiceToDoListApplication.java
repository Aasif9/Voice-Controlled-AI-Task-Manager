package com.example.Voice.To_Do.List;

import com.example.Voice.To_Do.List.Analyzers.MyAnalyzer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class VoiceToDoListApplication {
	public static void main(String[] args) {
		SpringApplication.run(VoiceToDoListApplication.class, args);
//		Set<String> set = new HashSet<>();
//		set.add("for");
//		MyAnalyzer myAnalyzer = new MyAnalyzer(set, );
//		String res = myAnalyzer.stem("nike shoes for mens");
//		System.out.println(res);
	}

}
