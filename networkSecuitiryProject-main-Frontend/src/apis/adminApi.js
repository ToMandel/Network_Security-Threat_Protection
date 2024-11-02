import axios from "axios";

const url ='https://localhost:8443';

export async function deleteAllUsers() {
    try {
      const response = await axios.delete(
        url+`/users`
      );
      return response;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  export async function getAllUsers() {
    try {
      const response = await axios.get(
        url+`/users`
      );
      return response;
    } catch (error) {
      console.error(error);
      return null;
    }
  }