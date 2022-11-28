package ui;

import controller.RankingController;
import entity.Rank;
import gateway.InMemoryRankGatewayImpl;
import gateway.RankGateway;
import gateway.RankGatewayImpl;
import presenter.RankingPresenter;
import ranking_use_case.RankingInteractor;
import ranking_use_case.RankingRequestModel;
import ranking_use_case.RankingResponseModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainDisplay extends JFrame {
    private RankGateway rankGateway = null;

    public static void main(String[] args) {
        MainDisplay queryDisplay = new MainDisplay();
        //queryDisplay.initInMemoryData();
        queryDisplay.initFileData();
        queryDisplay.display();
    }

    private void display() {
        this.setTitle("RankingSystem");
        this.setSize(1000, 800);
        this.setLayout(null);

        initFrame();

        this.setVisible(true);
    }

    private void initFrame() {
        Font font = new Font("Microsoft JhengHei", Font.BOLD, 27);
        JLabel titleLabel = new JLabel("Personal Ranking");
        titleLabel.setFont(font);
        titleLabel.setBounds(350, 30, 300, 40);
        this.add(titleLabel);

        JLabel idLabel = new JLabel("userId:");
        idLabel.setBounds(370, 150, 60, 30);
        this.add(idLabel);

        JTextField jTextField = new JTextField();
        jTextField.setBounds(480, 150, 200, 30);
        this.add(jTextField);

        JButton jButton = new JButton("Rank");
        jButton.setBounds(480, 300, 80, 30);
        this.add(jButton);

        jButton.addActionListener(e -> {
            String userId = jTextField.getText();
            if(userId == null || "".equals(userId)){
                JOptionPane.showMessageDialog(this, "Please input the userId");
                return;
            } else {
                showRank(userId);
            }
        });
    }

    private void showRank(String userId) {
        try{
            RankingRequestModel rankingRequestModel = new RankingRequestModel(userId, null);

            RankDisplay rankDisplay = new RankDisplay();
            RankingPresenter rankingPresenter = new RankingPresenter(rankDisplay);
            RankingInteractor rankingInteractor = new RankingInteractor(this.rankGateway, rankingRequestModel, rankingPresenter);

            RankingController rankingController = new RankingController(rankingInteractor);
            RankingResponseModel rankingResponseModel = rankingController.rank(rankingRequestModel);

            List<Rank> rankList = rankingResponseModel.getRankList();
            if(rankList == null || rankList.isEmpty()){
                JOptionPane.showMessageDialog(this, "Cannot find the user's data");
                return;
            }
            rankingPresenter.showRank(rankingResponseModel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot find user's data");
            e.printStackTrace();
        }
    }

    private void initFileData() {
        RankGatewayImpl rankGateway = new RankGatewayImpl();
        this.rankGateway = rankGateway;
    }

    public void initMemoryData(){
        InMemoryRankGatewayImpl inMemoryRankGateway = new InMemoryRankGatewayImpl();
        this. rankGateway = inMemoryRankGateway;
    }


}
