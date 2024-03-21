package com.kvbadev.wms.presentation.dataTransferObjects;

import java.util.Objects;

public class ItemDto {
    private String name;
    private String description;
    private Integer quantity;
    private Long netPrice;
    private Integer parcelId;

    public ItemDto() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(Long netPrice) {
        this.netPrice = netPrice;
    }
    public Integer getParcelId() {
        return parcelId;
    }

    public void setParcelId(Integer parcelId) {
        this.parcelId = parcelId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto that = (ItemDto) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(quantity, that.quantity) && Objects.equals(netPrice, that.netPrice) && Objects.equals(parcelId, that.parcelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, quantity, netPrice, parcelId);
    }


}
