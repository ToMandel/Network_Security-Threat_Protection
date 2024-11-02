import axios from "axios";

const url ='https://localhost:8443';


export async function Login(values) {
    try {
      const response = await axios.post(
        url+`/users/login`,
        values,
        {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error(error);
      return error.response.data.message;
    }
  }

  export async function Register(values) {
    try {
      const response = await axios.post(
        url+`/users`,
        values,
        {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error(error);
      return error.response;
    }
  }

  export async function updateUserPass(values){
    try {
      const response = await axios.put(
        url+`/users`,
        values,
        {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.log(error);
      return error;
    }
  }

export async function updateUser(values) {
    try {
      const response = await axios.put(
        url+`/users/forgot`,
        values,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error(error);
      return error.response.data.message;
    }
  }

  export async function forgotPassword(userName) {
    try {
      const response = await axios.post(
        url+`/users/forgot`,
        userName,
        {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error(error);
      return error.response.data.message;
    }
  }