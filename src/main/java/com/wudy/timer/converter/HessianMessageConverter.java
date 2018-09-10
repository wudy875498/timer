package com.wudy.timer.converter;

import com.wudy.timer.utils.SerializeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

@Slf4j
public class HessianMessageConverter implements MessageConverter {
    /**
     * Convert a Java object to a Message.
     *
     * @param object            the object to convert
     * @param messageProperties The message properties.
     * @return the Message
     * @throws MessageConversionException in case of conversion failure
     */
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        if(object == null) {
            return null;
        }
        if (messageProperties == null) {
            messageProperties = new MessageProperties();
        }
        byte[] bytes = SerializeUtils.serialize(object);
        messageProperties.setContentLength(bytes.length);
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT);
        messageProperties.setContentEncoding("UTF-8");
        return new Message(bytes, messageProperties);
    }

    /**
     * Convert from a Message to a Java object.
     *
     * @param message the message to convert
     * @return the converted Java object
     * @throws MessageConversionException in case of conversion failure
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {

        return SerializeUtils.deserialize(message.getBody());
    }
}
