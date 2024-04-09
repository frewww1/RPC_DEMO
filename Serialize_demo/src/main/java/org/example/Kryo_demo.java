package org.example;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import org.example.pojo.User;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Kryo_demo {
    public static byte[] serialize(User user) {
        Kryo kryo = new Kryo();
        kryo.register(User.class);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        kryo.writeObject(output,user);//写入null时会报错
        output.close();
        return bos.toByteArray();
    }
    public static User deserialize(byte[] bytes) {
        Kryo kryo = new Kryo();
        kryo.register(User.class);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Input input = new Input(bis);
        User user = kryo.readObject(input, User.class);//读出null时会报错
        input.close();
        return user;
    }
    public KryoPool newKryoPool() {
        return new KryoPool.Builder(() -> {
            final Kryo kryo = new Kryo();
//            kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(
//                    new StdInstantiatorStrategy()));
            return kryo;
        }).softReferences().build();
    }

    public static void main(String[] args) {
        User user = new User();
        System.out.print(serialize(user));
        KryoSerializer serializer = new KryoSerializer();
        System.out.print(serializer.serialize(user));
    }
}

