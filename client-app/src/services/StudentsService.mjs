import BaseService from './BaseService.mjs';

class StudentsService extends BaseService {
  constructor() {
    super('students_commands', 'students_data');
  }

  async all(callback) {
    this.sendMessage({
      command: 'get_students',
      payload: {
        id: 0,
        name: '',
        age: 0
      }
    })
      .then((response) => {
        this.receiveMessage(callback);
      });
  }

  async find(id, callback) {
    this.sendMessage({
      command: 'get_student',
      payload: {
        id: parseInt(id),
        name: '',
        age: 0
      }
    })
      .then((response) => {
        this.receiveMessage(callback);
      });
  }
  
  async create(body, callback) {
    this.sendMessage({
      command: 'create_student',
      payload: {
        id: 0,
        name: body.name,
        age: body.age
      }
    })
      .then((response) => {
        this.receiveMessage(callback);
      });
  }

  async update(id, body, callback) {
    this.sendMessage({
      command: 'update_student',
      payload: {
        id: parseInt(id),
        name: body.name,
        age: body.age
      }
    })
      .then((response) => {
        this.receiveMessage(callback);
      });
  }

  async delete(id, callback) {
    this.sendMessage({
      command: 'delete_student',
      payload: {
        id: parseInt(id),
        name: '',
        age: 0
      }
    })
      .then((response) => {
        this.receiveMessage(callback);
      });
  }
}

export default StudentsService;