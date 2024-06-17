import { Kafka } from 'kafkajs';

class BaseService {
  constructor(sendTopic, receiveTopic) {
    this.clientId = 'client-app';
    this.brokers = ['localhost:9092'];

    this.sendTopic = sendTopic;
    this.receiveTopic = receiveTopic;

    this.messageQueue = new Kafka({
      clientId: this.clientId,
      brokers: this.brokers,
    });
  }

  async sendMessage(message) {
    const producer = this.messageQueue.producer();
  
    await producer.connect();
    await producer.send({
      topic: this.sendTopic,
      messages: [
        { value: JSON.stringify(message) },
      ],
    });
  
    await producer.disconnect();
  }

  async receiveMessage(callback) {
    const consumer = this.messageQueue.consumer({ 
      groupId: 'client-group',
      heartbeatInterval: 10000,
      sessionTimeout: 60000,
    });
  
    await consumer.connect();
    await consumer.subscribe({ topic: this.receiveTopic, fromBeginning: true });
  
    await consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        callback(message.value.toString());
      },
    });
  }
}

export default BaseService;