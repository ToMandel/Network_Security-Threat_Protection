  import React, { useRef, useState } from "react";
  import {
    Layout,
    Button,
    Space,
    Typography,
    Input,
    Form,
    Checkbox,
    message,
  } from "antd";
  import { Register } from "../apis/usersapi";
  import { useNavigate } from "react-router-dom";


  const { Title } = Typography;
  const { Header, Footer, Content, Sider } = Layout;

  const headerStyle = {
    textAlign: "center",
    color: "#fff",
    height: 64,
    paddingInline: 50,
    lineHeight: "64px",
    backgroundColor: "#f4f2ee",
  };
  const contentStyle = {
    textAlign: "center",
    minHeight: 120,
    height: 450,
    lineHeight: "120px",
    color: "#fff",
    backgroundColor: "#efe7d2",
  };
  const footerStyle = {
    textAlign: "center",
    color: "#fff",
    backgroundColor: "#f4f2ee",
  };

  const RegisterPageContent = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [passwordValid, setPasswordValid] = useState(false);
    const [messageApi, contextHolder] = message.useMessage();
    const submissionShow = useRef(true);
    const [form] = Form.useForm();
    const [passwordsMatch, setPasswordsMatch] = useState(false);
    const navigate = useNavigate();

    const successMsg = (text) => {
      messageApi.open({
        type: "success",
        contenet: text,
      });
    };
    function timeout(delay) {
      return new Promise( res => setTimeout(res, delay) );
  }

    const errorMsg = (text) => {
      messageApi.open({
        type: "error",
        content: text,
      });
    };

    const onFinish = async (values) => {

      try{
      if (values.password === values.repeatPassword) {
          const { repeatPassword, ...cleanValues } = values;
          const result = await Register(cleanValues);
        if (result.ok){

          successMsg("Registration successful!");
          await timeout(1000);
          // navigate('/system-screen', { state: { booleanProp: true } });
        //  dispatch(SetAuthenticated(true));
          navigate("/SystemScreen", { state: { authentication: true } });
        }

      }

        else {errorMsg("passwords do not match")}
      } catch (error) {console.log(error);}
    };

    return (
      <>
        {contextHolder}
        <Form onFinish={onFinish}>
          <Form.Item
            name={["userName"]}
            // label="User Name"
            rules={[
              {
                required: true,
              },
            ]}
          >
            <Input placeholder="User Name"></Input>
          </Form.Item>
          <Form.Item
            name={["email"]}
            // label="Email"
            rules={[
              {
                type: "email",
                required: true,

              },
            ]}
          >
            <Input placeholder="Email"></Input>
          </Form.Item>

          <Form.Item
            name="password"
            rules={[
              {
                required: true,
              },
            ]}
            validateStatus={passwordValid ? "success" : "error"}
            help={
              !passwordValid && password.length > 0
                ? "Must contain at least 10 characters, one capital letter, one lowercase letter, one special character, and one digit"
                : null
            }
          >
            <Input.Password
              // type="password"
              placeholder="Password"
            />
          </Form.Item>

          <Form.Item
          name="repeatPassword"
          rules={[
            {
              required: true,
            },
          ]}
          validateStatus={passwordValid ? "success" : "error"}
        >
            <Input.Password
              value={repeatPassword}
              placeholder="Repeat Password"
              dependencies={['password']}
          rules={[
            { required: true, message: 'Please confirm your password!' },
            // { validator: validatePassword },
          ]}
              suffix={
                repeatPassword === password ? <Checkbox checked={true} /> : null
              }
            />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              Register
            </Button>
          </Form.Item>
        </Form>
      </>
    );
  };

  const RegisterPage = () => {
    return (
      <Space
        direction="vertical"
        style={{
          width: "100%",
        }}
        size={[0, 50]}
      >
        <Layout>
          <Header style={headerStyle}>
            <Title level={3} style={{ color: "#22545c" }}>
              Comunication_LTD
            </Title>
          </Header>
          <Layout>
            <Sider style={{ background: "#efe7d2" }}></Sider>
            <Layout>
              <Content style={contentStyle}>
                <RegisterPageContent />
              </Content>
            </Layout>
            <Sider style={{ background: "#efe7d2" }}></Sider>
          </Layout>
          <Footer style={footerStyle}></Footer>
        </Layout>
      </Space>
    );
  };
  export default RegisterPage;
