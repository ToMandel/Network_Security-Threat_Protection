import { Layout, Button, Space, Typography, Input, Form, message, } from "antd";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {updateUserPass} from "../apis/usersapi";

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

const ChangePasswordConmponent = () => {
  const [password, setPassword] = useState("");
  const [repeatPassword, setRepeatPassword] = useState("");
  const [passwordValid, setPasswordValid] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  const navigate = useNavigate();


  
  const successMsg = (text) => {
    messageApi.open({
      type: "success",
      contenet: text,
    });
  };

  const errorMsg = (text) => {
    messageApi.open({
      type: "error",
      content: text,
    });
  };
  function timeout(delay) {
    return new Promise( res => setTimeout(res, delay) );
}
  const SendTo = () => {
    navigate("/SystemScreen", { state: { authentication: true } });
};
  const onFinish = async (values) => {
    try{
        if (values.newPass === values.repeatPassword) {
            const { repeatPassword, ...cleanValues } = values;
            const response = await updateUserPass(cleanValues);
            if (!response) {
                await timeout(1000);
                SendTo();
            }
            else (errorMsg(response.data.message))
        }
        else (errorMsg("New Paswwords Don't Match"));
        
    } catch (error) {console.log(error);}

    //todo - check login and then update user.
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
        name={["password"]}
        // label="Email"
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
        <Input.Password placeholder="Old Password" />
      </Form.Item>

      <Form.Item
        name="newPass"
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
        <Input.Password placeholder="New Password" />
      </Form.Item>

      <Form.Item
        name="repeatPassword"
        rules={[
          {
            required: true,
          },
        ]}
      >
        <Input.Password
          value={repeatPassword}
          placeholder="Repeat Password"
          dependencies={["password"]}
          rules={[{ required: true, message: "Please confirm your password!" }]}
        />
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit">
          Change
        </Button>
      </Form.Item>
    </Form>
    </>
  );
};

const ChangePasswordPage = () => {
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
              <ChangePasswordConmponent />
            </Content>
          </Layout>
          <Sider style={{ background: "#efe7d2" }}></Sider>
        </Layout>
        <Footer style={footerStyle}></Footer>
      </Layout>
    </Space>
  );
};

export default ChangePasswordPage;
