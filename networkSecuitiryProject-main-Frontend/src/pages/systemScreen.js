import { Layout, Button, Space, Typography, Input } from "antd";
import { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";

const { Header, Footer, Content, Sider } = Layout;
const { Title } = Typography;

const footerStyle = {
  textAlign: "center",
  color: "#fff",
  backgroundColor: "#f4f2ee",
};
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

const Show = () => {};

const SystemScreenContent = () => {
  return (
    <div>
      <Title level={4}>Welcome</Title>
      <Title level={4}>Enter your new customer name:</Title>
      <Space.Compact>
        <Input />
        <Button type="primary" onClick={Show}>
          Submit
        </Button>
      </Space.Compact>
      <Title level={4}>Result:</Title>
      <Title level={4}></Title>
    </div>
  );
};

const SystemScreen = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const booleanProp = location.state;
 useEffect(() => {
    const checkIfAuthenticated = () => {
      if (!booleanProp) {
        navigate("/");
      } else {
        //   dispatch(SetAuthenticated(false));
      }
    };

    checkIfAuthenticated();
  }, []);

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
              <SystemScreenContent />
            </Content>
          </Layout>
          <Sider style={{ background: "#efe7d2" }}></Sider>
        </Layout>
        <Footer style={footerStyle}></Footer>
      </Layout>
    </Space>
  );
};
export default SystemScreen;
