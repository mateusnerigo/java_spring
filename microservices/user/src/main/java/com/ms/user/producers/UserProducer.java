package com.ms.user.producers;

import com.ms.user.dtos.EmailDto;
import com.ms.user.models.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(UserModel userModel) {
        EmailDto emailDto = new EmailDto();
        emailDto.setUserId(userModel.getUserId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Cadastro realizado com sucesso!");
        emailDto.setText(handleEmailToPublish(userModel.getName()));

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }

    private static String handleEmailToPublish(String userName) {
        return (
            userName + ", seja bem-vindo(a)! " +
            "\nAgradecemos o seu cadastro. " +
            "Aproveite agora todos os recursos da nossa plataforma!"
        );
    }
}
