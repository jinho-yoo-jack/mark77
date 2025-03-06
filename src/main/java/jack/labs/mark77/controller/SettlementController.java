package jack.labs.mark77.controller;

import jack.labs.mark77.dto.tri_count.CreateSettlement;
import jack.labs.mark77.dto.tri_count.JoinSettlement;
import jack.labs.mark77.dto.tri_count.ReqCreateSettlement;
import jack.labs.mark77.dto.tri_count.ReqJoinSettlement;
import jack.labs.mark77.entity.tricount.Settlement;
import jack.labs.mark77.entity.tricount.SettlementUsers;
import jack.labs.mark77.global.ApiResponse;
import jack.labs.mark77.service.tricount.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settlement")
@RequiredArgsConstructor
@Slf4j
public class SettlementController {
    private final SettlementService settlementService;

    @PostMapping("/api/v1/settlement/create")
    public ResponseEntity<ApiResponse<Settlement>> createSettlement(@RequestBody ReqCreateSettlement requestMessage) {
        return ResponseEntity.ok(ApiResponse.success(settlementService.create(CreateSettlement.of(requestMessage))));
    }

    @PostMapping("/api/v1/settlement/join")
    public ResponseEntity<ApiResponse<SettlementUsers>> joinSettlement(@RequestBody ReqJoinSettlement requestMessage) {
        return ResponseEntity.ok(ApiResponse.success(settlementService.join(JoinSettlement.of(requestMessage.getSettlementId()))));
    }

    @PostMapping("/api/v1/register/expense")
    public ResponseEntity<ApiResponse<String>> registerExpense() {
        return null;
    }
}
